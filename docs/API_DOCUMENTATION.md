# Atrang 2026–27 REST API Specification

Comprehensive REST API contract for public festival participants and authenticated administrators.

## Base URL
- Local: `http://localhost:8080/api`

---

## 1. Authentication Endpoints

### 1.1 `POST /api/auth/login`
Authenticates an administrator and establishes a server-managed HttpOnly session cookie (`ATRANG_SESSION_ID`).

- **Request Body:**
```json
{
  "username": "admin",
  "password": "your_secure_password"
}
```
- **Response `200 OK`:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "authenticated": true,
    "message": "Authentication successful",
    "user": {
      "id": 1,
      "username": "admin",
      "fullName": "Chief Technical Administrator",
      "email": "admin@atrang.iiitt.ac.in",
      "role": "ROLE_SUPERADMIN",
      "isActive": true,
      "createdAt": "2026-10-26T10:00:00Z"
    }
  },
  "timestamp": "2026-10-26T10:00:01Z"
}
```
- **Response `401 Unauthorized`:** Bad credentials.

### 1.2 `POST /api/auth/logout`
Invalidates active server session and clears credentials.

### 1.3 `GET /api/auth/me`
Returns details of the currently authenticated administrator. Returns `401 Unauthorized` if unauthenticated.

---

## 2. Public Endpoints (No Auth Required)

### 2.1 `GET /api/public/events`
Returns list of published festival events.
- **Query Parameters:**
  - `category` (optional, string): filter by category code (e.g. `coding`, `gaming`)
  - `search` (optional, string): search by title or summary

### 2.2 `GET /api/public/events/{slug}`
Returns complete details and schedule sessions for a published event.
- **Response `200 OK`:** Returns `EventDetailDto`.
- **Response `404 Not Found`:** Event does not exist or is unpublished.

### 2.3 `GET /api/public/categories`
Returns active categories with published event counts and theme colors.

---

## 3. Admin Protected Endpoints (Requires `ROLE_ADMIN` or `ROLE_SUPERADMIN`)

### 3.1 `GET /api/admin/events`
Paginated event list with drafts and published events.
- **Query Parameters:** `page`, `size`, `categoryId`, `isPublished`, `search`.

### 3.2 `POST /api/admin/events`
Creates a new festival event. Automatically records audit log.

### 3.3 `PUT /api/admin/events/{id}`
Updates existing event and its schedule sessions.

### 3.4 `PATCH /api/admin/events/{id}/publish`
Toggles publication status between draft and published.

### 3.5 `DELETE /api/admin/events/{id}`
Deletes an event and its associated sessions.

### 3.6 `GET /api/admin/categories`
Returns all categories including inactive ones.

### 3.7 `POST /api/admin/categories`
Creates a new event category.

### 3.8 `PUT /api/admin/categories/{id}`
Updates category name, description, hex color, or icon.

### 3.9 `DELETE /api/admin/categories/{id}`
Deletes an empty category. Returns `400 Bad Request` if category contains events.

### 3.10 `GET /api/admin/dashboard/summary`
Returns system metrics: total events, published events, draft events, total categories, sessions, audit log count, and category breakdown.

### 3.11 `GET /api/admin/audit-logs`
Returns paginated database audit trail records.
