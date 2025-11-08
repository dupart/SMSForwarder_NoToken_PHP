
# Backend PHP (SQLite)
- Dépose le dossier **BackendPHP** sur ton hébergement (Apache/Nginx avec PHP 8+).
- L'URL de réception est l'URL de `index.php` (ex: `https://tonsite.com/sms/index.php`).
- L'app Android enverra un JSON `{from, body, received_at}` en POST.
- Les SMS sont enregistrés dans **sms.sqlite** (fichier créé automatiquement).
- Ouvrir `index.php` en GET affiche une petite interface pour consulter les derniers messages.
