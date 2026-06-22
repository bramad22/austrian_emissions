# Austrian Emissions Database

**Database:** PostgreSQL

## **Tables**

---

### **`Region`**
**Description:** Stores information about geographical regions in Austria.

**Columns:**

| Column   | Type      | Description                     | Constraints          |
|----------|-----------|---------------------------------|----------------------|
| `id`     | VARCHAR   | Unique identifier for the region. | **Primary Key**      |
| `name`   | VARCHAR   | Name of the region.              | Not null             |

**Relationships:**
- One-to-Many with `RegionData` (via `region_id` in `RegionData`).

---

### **`RegionData`**
**Description:** Stores detailed emissions data for each region, sector, and year.

**Columns:**

| Column        | Type      | Description                          | Constraints          |
|---------------|-----------|--------------------------------------|----------------------|
| `id`          | BIGINT    | Unique identifier for the record.    | **Primary Key**      |
| `source`      | VARCHAR   | Source of the emissions data.        |                      |
| `value`       | BIGINT    | Emission value.                      |                      |
| `year`        | INTEGER   | Year of the data.                    | Not null             |
| `region_id`   | VARCHAR   | Foreign key referencing `Region.id`. | **Foreign Key**      |
| `sector`      | VARCHAR   | Sector (e.g., "Industrie", "Gebäude"). | Not null          |
| `pollutant`   | VARCHAR   | Type of pollutant (e.g., "CO2").      |                      |
| `unit`        | VARCHAR   | Unit of measurement (e.g., "t CO2eq"). |                   |
| `classification` | VARCHAR | Classification of the data.          |                      |

**Relationships:**
- Many-to-One with `Region` (via `region_id`).

---
### **`Population`**
**Description:** Stores population data for each region and year.

**Columns:**

| Column      | Type      | Description                     | Constraints          |
|-------------|-----------|---------------------------------|----------------------|
| `id`        | BIGINT    | Unique identifier for the record. | **Primary Key**      |
| `region`    | VARCHAR   | Name of the region.             | Not null             |
| `year`      | INTEGER   | Year of the population data.   | Not null             |
| `population`| BIGINT    | Population count.               | Not null             |

---
## **Relationships Overview**

- **`Region` → `RegionData`**: One region can have multiple emissions data entries.
- **`Population`**: Standalone table, linked to `Region` and `RegionData` by `region` and `year` fields.
