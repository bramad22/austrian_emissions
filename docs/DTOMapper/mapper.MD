# RegionDataMapper – Documentation

The `RegionDataMapper` class provides a utility method to convert Java objects into a `Map<String, Object>` representation, specifically for fields annotated with `@ToDto`. This is used to transform objects into DTOs dynamically.

---

## **Method**

### **`toDto(Object o)`**
**Description:** Converts an object into a `Map<String, Object>` by extracting fields annotated with `@ToDto`.

**Parameters:**
- `o` (Object): The object to be converted.

**Returns:**
- `Map<String, Object>`: A map where keys are the field names (or custom keys specified in the `@ToDto` annotation) and values are the corresponding field values.

**Behavior:**
- Only fields annotated with `@ToDto` are included in the result.
- If the `@ToDto` annotation specifies a custom `key`, it is used; otherwise, the field name is used as the key.
- If a field cannot be accessed, a `RuntimeException` is thrown.