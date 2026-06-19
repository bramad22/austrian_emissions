# Austrian Emissions API – Controller Documentation

**Class:** `Controller`
**Base Path:** `/api/v1/emission`

The `Controller` class provides REST endpoints for accessing Austrian regional emissions data.

> **Info:** The Total Emissions for each Region is represented by the sector `KSG`

---
## **Endpoints**

### **1. Test Endpoint**
**Path:** `GET /api/v1/emission/test`
**Description:** Returns a simple test message to verify API availability.
**Response:** `ResponseEntity<String>` (e.g., `"Servas!"`).

---

### **2. Detailed Emissions Data**
**Path:** `GET /api/v1/emission/emissionDetailed`
**Description:** Returns detailed emissions data filtered by region, sector, or year range.

- **Parameters (optional):**
    - `region` (String)
    - `sector` (String)
    - `year` (String)
    - `startYear` (String)
    - `endYear` (String)

**Response:** `List<Map<String, Object>>`

---

### **3. Total Emissions (KSG Sector)**
**Path:** `GET /api/v1/emission/totalEmissions`
**Description:** Returns total emissions filtered by region, or year range.
- **Parameters (optional):**
    - `region` (String)
    - `year` (String)
    - `startYear` (String)
    - `endYear` (String)
  
**Response:** `List<Map<String, Object>>`

---

### **4. Emissions Per Capita**
**Path:** `GET /api/v1/emission/emissionPerCapita`
**Description:** Returns emissions per capita filtered by region, sector, or year range.

- **Parameters (optional):**
    - `region` (String)
    - `sector` (String)
    - `year` (String)
    - `startYear` (String)
    - `endYear` (String)

**Response:** `List<Map<String, Object>>`

---

### **5. Total Emissions Per Capita**
**Path:** `GET /api/v1/emission/totalEmissionPerCapita`

**Description:** Returns total emissions per Capita filtered by region, or year range.
- **Parameters (optional):**
    - `region` (String)
    - `year` (String)
    - `startYear` (String)
    - `endYear` (String)
  
**Response:** `List<Map<String, Object>>`

---
### **6. Total Emissions Per Sector and Year**
**Path:** `GET /api/v1/emission/totalEmissionPerSector`
**Description:** Returns total emissions grouped by year and sector.

- **Parameters (optional):**
  - `sector` (String)
  - `year` (String)
  - `startYear` (String)
  - `endYear` (String)

**Response:** `Map<Integer, Map<String, Long>>`

---
### **7. Percentage Change**
**Path:** `GET /api/v1/emission/percentageChange`
**Description:** Calculates the percentage change in emissions in respect to the year before.
- **Parameters (optional):**
    - `region` (String)
    - `sector` (String)
    - `year` (String)
    - `startYear` (String)
    - `endYear` (String)
  
**Response:** `List<Map<String, Object>>`

---
### **8. Heating Share**
**Path:** `GET /api/v1/emission/heatingShare`
**Description:** Calculates the heating share of emissions for a specific year.
- **Parameters:**
  - `year` (int, **required**)

**Response:** `Map<String, Object>`
