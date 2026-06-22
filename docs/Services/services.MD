# RegionDataService – Documentation

The `RegionDataService` class provides business logic for querying, filtering, and calculating emissions data. It interacts with repositories and external APIs to retrieve and process data.

---

> ## **Methods**


### **`filterEmissionData(String region, String sector, String year, String startYear, String endYear)`**
**Description:** Filters `RegionData` entities based on the provided criteria.

**Parameters:**
- `region` (String, optional): Filter by region name.
- `sector` (String, optional): Filter by sector.
- `year` (String, optional): Filter by exact year.
- `startYear` (String, optional): Filter by start year (inclusive).
- `endYear` (String, optional): Filter by end year (inclusive).

**Returns:**
- `Stream<RegionData>`: A stream of filtered `RegionData` objects.

---

### **`getEmissionDetailed(String region, String sector, String year, String startYear, String endYear)`**
**Description:** Returns detailed emissions data.

**Parameters:** Same as `filterEmissionData`.

**Returns:**
- `List<Map<String, Object>>`: List of DTOs representing the filtered emissions data.
---

### **`emissionsPerCapita(String region, String sector, String year, String startYear, String endYear)`**
**Description:** Calculates emissions per capita for the filtered data.

**Parameters:** Same as `filterEmissionData`.

**Returns:**
- `List<Map<String, Object>>`: List of maps containing:
    - `region` (String)
    - `year` (int)
    - `sector` (String)
    - `valuePerCapita` (double)

**Behavior:**
- Retrieves population data for each region and year using `PopulationRepository`.
- Calculates emissions per capita as `emissionValue / population`.

---

### **`totalEmissionsPerYearPerSector(String sector, String year, String startYear, String endYear)`**
**Description:** Groups emissions data by year and sector, summing the values.

**Parameters:**
- `sector` (String, optional): Filter by sector.
- `year` (String, optional): Filter by exact year.
- `startYear` (String, optional): Filter by start year (inclusive).
- `endYear` (String, optional): Filter by end year (inclusive).

**Returns:**
- `Map<Integer, Map<String, Long>>`:
    - **Key:** Year (Integer).
    - **Value:** Map where:
        - **Key:** Sector (String).
        - **Value:** Total emissions for the sector in that year (Long).

---

### **`percentageChange(String region, String sector, String year, String startYear, String endYear)`**
**Description:** Calculates the percentage change in emissions between consecutive years for each region and sector.

**Parameters:** Same as `filterEmissionData`.

**Returns:**
- `List<Map<String, Object>>`: List of maps containing:
    - `region` (String)
    - `sector` (String)
    - `year` (int)
    - `percentageChange` (double)
    - `unit` (String, always `"t CO2eq"`)

**Behavior:**
- Sorts the filtered data by region, sector, and year.
- Compares each entry with the previous one to calculate the percentage change.

---

---
### **`calculateHeatingShare(int year)`**
**Description:** Calculates the share of heating emissions relative to total building emissions for a given year.

**Parameters:**
- `year` (int, **required**): Year for which to calculate the heating share.

**Returns:**
- `Map<String, Object>`:
    - **Key:** Region name (String).
    - **Value:** Heating share as a percentage (double, rounded to 3 decimal places).
    - If the year is outside the range **2004–2022**, returns an error map with the message:
      `"There are only values available between 2004 and 2022!"`

**Behavior:**
- Fetches heating emissions data from an external API (`http://10.143.165.155:8080/api/v1/heating/by_region/co2_ton_equ`).
- Retrieves building emissions data for the specified year from `RegionDataRepository`.

---