# Austrian Emissions API

> **Spring Boot REST API für Emissionsdaten von Österreich** – Abfrage von CO₂-Äquivalent Emissionen gegliedert in Region, Sektor und Jahr mit verschiedenen Auswertungsmöglichkeiten.

---
### Technologies
- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### 1. clone Project & start

```bash
# clone Repository
git clone https://github.com/bramad22/austrian_emissions.git
cd austrian_emissions

# Start all Container (Backend + Database) with Docker Compose
docker-compose up -d