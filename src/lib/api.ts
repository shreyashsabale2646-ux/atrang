/**
 * Atrang 2026–27 REST API Client
 * Centralized, typed API client handling HTTP sessions and backend communication.
 */

export const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080/api";

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
  timestamp?: string;
}

export interface AdminUser {
  id: number;
  username: string;
  fullName: string;
  email: string;
  role: string;
  isActive: boolean;
  createdAt: string;
}

export interface Category {
  id: number;
  code: string;
  name: string;
  description?: string;
  colorHex: string;
  iconName: string;
  displayOrder: number;
  isActive: boolean;
  eventCount?: number;
  createdAt?: string;
}

export interface EventSession {
  id?: number;
  eventId?: number;
  festDay: number;
  startTime: string;
  endTime: string;
  sessionTitle: string;
  venue?: string;
  details?: string;
}

export interface EventSummary {
  id: number;
  slug: string;
  title: string;
  summary: string;
  categoryId: number;
  categoryCode: string;
  categoryName: string;
  categoryColor: string;
  categoryIcon: string;
  participation: string;
  teamSize?: string;
  duration?: string;
  prize?: string;
  venue: string;
  fee?: string;
  registrationUrl?: string;
  isPublished: boolean;
  displayOrder: number;
  sessionCount: number;
  createdAt: string;
}

export interface EventDetail extends EventSummary {
  description: string;
  organizer: string;
  eligibility?: string;
  registrationDeadline?: string;
  updatedAt?: string;
  sessions: EventSession[];
}

export interface EventFormData {
  categoryId: number;
  slug: string;
  title: string;
  summary: string;
  description: string;
  organizer?: string;
  participation: "Individual" | "Team" | "Individual or Team";
  teamSize?: string;
  duration?: string;
  prize?: string;
  venue: string;
  eligibility?: string;
  fee?: string;
  registrationUrl?: string;
  registrationDeadline?: string;
  isPublished?: boolean;
  displayOrder?: number;
  sessions?: EventSession[];
}

export interface CategoryFormData {
  code: string;
  name: string;
  description?: string;
  colorHex: string;
  iconName: string;
  displayOrder: number;
  isActive?: boolean;
}

export interface CategoryMetric {
  categoryId: number;
  categoryCode: string;
  categoryName: string;
  colorHex: string;
  iconName: string;
  totalEvents: number;
  publishedEvents: number;
  draftEvents: number;
}

export interface DashboardSummary {
  totalEvents: number;
  publishedEvents: number;
  draftEvents: number;
  totalCategories: number;
  totalSessions: number;
  totalAuditLogs: number;
  categoryMetrics: CategoryMetric[];
}

export interface AuditLog {
  id: number;
  eventId?: number;
  adminId?: number;
  action: string;
  details: string;
  performedBy: string;
  ipAddress?: string;
  createdAt: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

class ApiError extends Error {
  status: number;
  constructor(message: string, status: number) {
    super(message);
    this.status = status;
    this.name = "ApiError";
  }
}

async function request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;
  const headers = {
    "Content-Type": "application/json",
    Accept: "application/json",
    ...(options.headers || {}),
  };

  const response = await fetch(url, {
    ...options,
    headers,
    credentials: "include", // Include HttpOnly session cookie
  });

  const text = await response.text();
  let json: any = null;
  if (text) {
    try {
      json = JSON.parse(text);
    } catch {
      // not JSON
    }
  }

  if (!response.ok) {
    const errorMsg =
      json?.message || json?.error || `HTTP error ${response.status}: ${response.statusText}`;
    throw new ApiError(errorMsg, response.status);
  }

  return (json?.data !== undefined ? json.data : json) as T;
}

export const api = {
  // Auth
  login: (username: string, password: string) =>
    request<{ authenticated: boolean; user: AdminUser }>("/auth/login", {
      method: "POST",
      body: JSON.stringify({ username, password }),
    }),

  logout: () =>
    request<string>("/auth/logout", {
      method: "POST",
    }),

  getMe: () => request<AdminUser>("/auth/me"),

  // Public
  getPublicEvents: (category?: string, search?: string) => {
    const params = new URLSearchParams();
    if (category) params.set("category", category);
    if (search) params.set("search", search);
    const qs = params.toString() ? `?${params.toString()}` : "";
    return request<EventSummary[]>(`/public/events${qs}`);
  },

  getPublicEventBySlug: (slug: string) =>
    request<EventDetail>(`/public/events/${encodeURIComponent(slug)}`),

  getPublicCategories: () => request<Category[]>("/public/categories"),

  // Admin Events
  getAdminEvents: (params?: { categoryId?: number; isPublished?: boolean; search?: string; page?: number; size?: number }) => {
    const query = new URLSearchParams();
    if (params?.categoryId) query.set("categoryId", String(params.categoryId));
    if (params?.isPublished !== undefined) query.set("isPublished", String(params.isPublished));
    if (params?.search) query.set("search", params.search);
    if (params?.page !== undefined) query.set("page", String(params.page));
    if (params?.size !== undefined) query.set("size", String(params.size));
    const qs = query.toString() ? `?${query.toString()}` : "";
    return request<PageResponse<EventSummary>>(`/admin/events${qs}`);
  },

  getAdminEventById: (id: number) => request<EventDetail>(`/admin/events/${id}`),

  createEvent: (data: EventFormData) =>
    request<EventDetail>("/admin/events", {
      method: "POST",
      body: JSON.stringify(data),
    }),

  updateEvent: (id: number, data: EventFormData) =>
    request<EventDetail>(`/admin/events/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),

  togglePublishEvent: (id: number) =>
    request<EventDetail>(`/admin/events/${id}/publish`, {
      method: "PATCH",
    }),

  deleteEvent: (id: number) =>
    request<string>(`/admin/events/${id}`, {
      method: "DELETE",
    }),

  // Admin Categories
  getAdminCategories: () => request<Category[]>("/admin/categories"),

  createCategory: (data: CategoryFormData) =>
    request<Category>("/admin/categories", {
      method: "POST",
      body: JSON.stringify(data),
    }),

  updateCategory: (id: number, data: Partial<CategoryFormData>) =>
    request<Category>(`/admin/categories/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),

  deleteCategory: (id: number) =>
    request<string>(`/admin/categories/${id}`, {
      method: "DELETE",
    }),

  // Admin Dashboard & Reports
  getDashboardSummary: () => request<DashboardSummary>("/admin/dashboard/summary"),

  getCategoryMetrics: () => request<CategoryMetric[]>("/admin/reports/events-by-category"),

  getAuditLogs: (page = 0, size = 20) =>
    request<PageResponse<AuditLog>>(`/admin/audit-logs?page=${page}&size=${size}`),
};
