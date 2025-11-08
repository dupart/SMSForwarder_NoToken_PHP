
# SMSForwarder (No Token, PHP)
- Configure **plusieurs numéros** (séparés par des virgules) et **URL du script PHP** dans *Configuration*.
- L'app capte les SMS entrants et POST un JSON au script PHP : `{ from, body, received_at }`.
- Aucune authentification requise (tu peux en ajouter côté PHP si tu veux).
