# POJOs and DTOs Documentation

---

> ## **Entities**

---

### **`Population`**

**Description:**
Entity representing population data for a region and year.


**Fields:**

| Field       | Type   | Description                     |
|-------------|--------|---------------------------------|
| `id`        | Long   | Unique identifier.              |
| `region`    | String | Name of the region.             |
| `year`      | int    | Year of the population data.   |
| `population`| long   | Population count.               |

---

### **`Region`**

**Description:**
Entity representing a geographical region.

**Fields:**

| Field | Type               | Description                          |
|-------|--------------------|--------------------------------------|
| `id`  | String             | Unique identifier.                   |
| `name`| String             | Name of the region.                  |
| `data`| `List<RegionData>` | List of `RegionData` associated with the region. |

---
### **`RegionData`**

**Description:**
Entity representing emissions data for a region, sector, and year.

**Fields:**

| Field       | Type    | Description                          |
|-------------|---------|--------------------------------------|
| `id`        | Long    | Unique identifier.                   |
| `source`    | String  | Source of the data.                  |
| `value`     | Long    | Emission value (mapped to `"emissionValue"` in DTOs). |
| `year`      | int     | Year of the data.                    |
| `region`    | `Region`| Associated region.                   |
| `sector`    | String  | Sector (mapped from JSON field `"sektor"` and to `"sektor_name"` in DTOs). |
| `pollutant` | String  | Type of pollutant.                   |
| `unit`      | String  | Unit of measurement (included in DTOs). |
| `classification` | String | Classification of the data.      |