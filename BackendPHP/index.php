<?php
declare(strict_types=1);

$dbFile = __DIR__ . '/sms.sqlite';
$pdo = new PDO('sqlite:' . $dbFile);
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$pdo->exec("CREATE TABLE IF NOT EXISTS sms_messages (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  from_number TEXT NOT NULL,
  body TEXT NOT NULL,
  received_at INTEGER NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
)");

$method = $_SERVER['REQUEST_METHOD'] ?? 'GET';
$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

if ($method === 'POST') {
  $raw = file_get_contents('php://input');
  $json = json_decode($raw, true);
  if (!is_array($json) || empty($json['from']) || !isset($json['body'])) {
    http_response_code(400);
    echo json_encode(['ok'=>false, 'error'=>'bad_payload']);
    exit;
  }
  $from = (string)$json['from'];
  $body = (string)$json['body'];
  $received_at = isset($json['received_at']) ? (int)$json['received_at'] : (int)(microtime(true)*1000);

  $stmt = $pdo->prepare("INSERT INTO sms_messages(from_number, body, received_at) VALUES(?,?,?)");
  $stmt->execute([$from, $body, $received_at]);

  header('Content-Type: application/json');
  echo json_encode(['ok'=>true]);
  exit;
}

# GET: simple UI
$stmt = $pdo->query("SELECT id, from_number, body, datetime(received_at/1000, 'unixepoch', 'localtime') as received_at_dt, created_at FROM sms_messages ORDER BY id DESC LIMIT 200");
$rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
?><!doctype html>
<html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>SMS Inbox</title>
<style>
body{font-family:system-ui,Arial;margin:20px}
table{border-collapse:collapse;width:100%}
th,td{border:1px solid #ddd;padding:8px}
th{background:#f5f5f5;text-align:left}
td pre{white-space:pre-wrap;word-wrap:break-word;margin:0}
</style>
</head><body>
<h1>📩 SMS reçus</h1>
<p>URL de réception (à mettre dans l'application Android) : <code><?php echo htmlspecialchars((isset($_SERVER['HTTPS'])?'https':'http').'://'.$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']); ?></code></p>
<table>
<thead><tr><th>#</th><th>De</th><th>Message</th><th>Reçu</th><th>Créé</th></tr></thead>
<tbody>
<?php foreach ($rows as $r): ?>
<tr>
  <td><?php echo (int)$r['id']; ?></td>
  <td><?php echo htmlspecialchars($r['from_number']); ?></td>
  <td><pre><?php echo htmlspecialchars($r['body']); ?></pre></td>
  <td><?php echo htmlspecialchars($r['received_at_dt']); ?></td>
  <td><?php echo htmlspecialchars($r['created_at']); ?></td>
</tr>
<?php endforeach; ?>
</tbody>
</table>
</body></html>
