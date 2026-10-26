"use client";

import React, { useEffect, useState, useMemo } from "react";
import {
  Calendar,
  Plus,
  Search,
  Filter,
  CheckCircle,
  Eye,
  EyeOff,
  Edit2,
  Trash2,
  AlertCircle,
  X,
  Loader2,
  Save,
  Clock,
  MapPin,
  Award,
} from "lucide-react";
import {
  api,
  Category,
  EventSummary,
  EventDetail,
  EventFormData,
  EventSession,
} from "@/lib/api";

export default function AdminEventsPage() {
  const [events, setEvents] = useState<EventSummary[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Filters
  const [searchTerm, setSearchTerm] = useState("");
  const [categoryFilter, setCategoryFilter] = useState<number | "ALL">("ALL");
  const [statusFilter, setStatusFilter] = useState<"ALL" | "PUBLISHED" | "DRAFT">("ALL");

  // Modal State
  const [modalOpen, setModalOpen] = useState(false);
  const [editingEventId, setEditingEventId] = useState<number | null>(null);
  const [deleteConfirmId, setDeleteConfirmId] = useState<number | null>(null);
  const [saving, setSaving] = useState(false);
  const [actionSuccess, setActionSuccess] = useState<string | null>(null);

  // Form State
  const initialForm: EventFormData = {
    categoryId: 1,
    slug: "",
    title: "",
    summary: "",
    description: "",
    organizer: "Atrang Technical Committee",
    participation: "Individual",
    teamSize: "1 member",
    duration: "2 hours",
    prize: "₹10,000",
    venue: "Main Campus Auditorium",
    eligibility: "Open to all college students",
    fee: "Free",
    registrationUrl: "https://unstop.com",
    isPublished: true,
    displayOrder: 1,
    sessions: [
      {
        festDay: 1,
        startTime: "10:00 AM",
        endTime: "12:00 PM",
        sessionTitle: "Round 1 — Preliminary Contest",
        venue: "Computer Lab 1",
        details: "Elimination round",
      },
    ],
  };

  const [formData, setFormData] = useState<EventFormData>(initialForm);

  const fetchEventsAndCategories = async () => {
    setLoading(true);
    setError(null);
    try {
      const [cats, evts] = await Promise.all([
        api.getAdminCategories(),
        api.getAdminEvents({ size: 100 }),
      ]);
      setCategories(cats);
      setEvents(evts.content || []);
      if (cats.length > 0 && formData.categoryId === 1) {
        setFormData((prev) => ({ ...prev, categoryId: cats[0].id }));
      }
    } catch (err: any) {
      setError(err?.message || "Failed to load events data");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEventsAndCategories();
  }, []);

  // Filtered Events
  const filteredEvents = useMemo(() => {
    return events.filter((e) => {
      const matchesSearch =
        !searchTerm ||
        e.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        e.slug.toLowerCase().includes(searchTerm.toLowerCase()) ||
        e.summary.toLowerCase().includes(searchTerm.toLowerCase());

      const matchesCategory =
        categoryFilter === "ALL" || e.categoryId === categoryFilter;

      const matchesStatus =
        statusFilter === "ALL" ||
        (statusFilter === "PUBLISHED" && e.isPublished) ||
        (statusFilter === "DRAFT" && !e.isPublished);

      return matchesSearch && matchesCategory && matchesStatus;
    });
  }, [events, searchTerm, categoryFilter, statusFilter]);

  // Handle open create
  const handleOpenCreate = () => {
    setEditingEventId(null);
    setFormData({
      ...initialForm,
      categoryId: categories.length > 0 ? categories[0].id : 1,
    });
    setModalOpen(true);
  };

  // Handle open edit
  const handleOpenEdit = async (id: number) => {
    setLoading(true);
    try {
      const detail = await api.getAdminEventById(id);
      setEditingEventId(id);
      setFormData({
        categoryId: detail.categoryId,
        slug: detail.slug,
        title: detail.title,
        summary: detail.summary,
        description: detail.description,
        organizer: detail.organizer,
        participation: detail.participation as any,
        teamSize: detail.teamSize || "",
        duration: detail.duration || "",
        prize: detail.prize || "",
        venue: detail.venue,
        eligibility: detail.eligibility || "",
        fee: detail.fee || "",
        registrationUrl: detail.registrationUrl || "",
        isPublished: detail.isPublished,
        displayOrder: detail.displayOrder,
        sessions: detail.sessions && detail.sessions.length > 0 ? detail.sessions : [],
      });
      setModalOpen(true);
    } catch (err: any) {
      setError("Failed to fetch event details: " + err?.message);
    } finally {
      setLoading(false);
    }
  };

  // Auto-slug generator on title change during create
  const handleTitleChange = (val: string) => {
    setFormData((prev) => {
      const updated = { ...prev, title: val };
      if (!editingEventId) {
        updated.slug = val
          .toLowerCase()
          .replace(/[^a-z0-9]+/g, "-")
          .replace(/^-+|-+$/g, "");
      }
      return updated;
    });
  };

  // Save Event
  const handleSubmitEvent = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      if (editingEventId) {
        await api.updateEvent(editingEventId, formData);
        setActionSuccess(`Updated event: "${formData.title}"`);
      } else {
        await api.createEvent(formData);
        setActionSuccess(`Created event: "${formData.title}"`);
      }
      setModalOpen(false);
      fetchEventsAndCategories();
    } catch (err: any) {
      setError(err?.message || "Failed to save event");
    } finally {
      setSaving(false);
    }
  };

  // Toggle publish
  const handleTogglePublish = async (id: number) => {
    try {
      const updated = await api.togglePublishEvent(id);
      setEvents((prev) =>
        prev.map((ev) => (ev.id === id ? { ...ev, isPublished: updated.isPublished } : ev))
      );
      setActionSuccess(
        `${updated.isPublished ? "Published" : "Unpublished"} "${updated.title}"`
      );
    } catch (err: any) {
      setError(err?.message || "Failed to update publication status");
    }
  };

  // Delete event
  const handleDeleteConfirm = async () => {
    if (!deleteConfirmId) return;
    try {
      await api.deleteEvent(deleteConfirmId);
      setEvents((prev) => prev.filter((ev) => ev.id !== deleteConfirmId));
      setActionSuccess("Event deleted successfully");
      setDeleteConfirmId(null);
    } catch (err: any) {
      setError(err?.message || "Failed to delete event");
    }
  };

  // Add session slot in modal
  const handleAddSession = () => {
    setFormData((prev) => ({
      ...prev,
      sessions: [
        ...(prev.sessions || []),
        {
          festDay: (prev.sessions?.length || 0) + 1,
          startTime: "02:00 PM",
          endTime: "04:00 PM",
          sessionTitle: "Session Title",
          venue: prev.venue,
          details: "",
        },
      ],
    }));
  };

  // Remove session slot in modal
  const handleRemoveSession = (idx: number) => {
    setFormData((prev) => ({
      ...prev,
      sessions: prev.sessions?.filter((_, i) => i !== idx),
    }));
  };

  return (
    <div className="space-y-6">
      {/* Header bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b-2 border-black">
        <div>
          <h1 className="font-pixel text-2xl sm:text-3xl text-gold tracking-wide uppercase">
            Festival Events Management
          </h1>
          <p className="text-xs text-neutral-400 font-mono mt-1">
            Create, Edit, Schedule &amp; Publish Atrang 2026–27 Competitions
          </p>
        </div>

        <button
          onClick={handleOpenCreate}
          className="flex items-center gap-2 px-4 py-2.5 bg-[#15803d] hover:bg-[#16a34a] text-white border-2 border-black shadow-[3px_3px_0px_#000] font-pixel text-xs tracking-wider uppercase active:translate-y-0.5 transition cursor-pointer"
        >
          <Plus className="w-4 h-4" />
          <span>Create New Event</span>
        </button>
      </div>

      {/* Notifications */}
      {actionSuccess && (
        <div className="p-3 bg-[#064e3b] border-2 border-[#059669] text-emerald-200 text-xs font-mono flex items-center justify-between shadow-[2px_2px_0px_#000]">
          <div className="flex items-center gap-2">
            <CheckCircle className="w-4 h-4 text-emerald-300" />
            <span>{actionSuccess}</span>
          </div>
          <button
            onClick={() => setActionSuccess(null)}
            className="text-emerald-400 hover:text-white"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      )}

      {error && (
        <div className="p-3 bg-[#450a0a] border-2 border-[#b91c1c] text-red-200 text-xs font-mono flex items-center justify-between shadow-[2px_2px_0px_#000]">
          <div className="flex items-center gap-2">
            <AlertCircle className="w-4 h-4 text-red-300 shrink-0" />
            <span>{error}</span>
          </div>
          <button onClick={() => setError(null)} className="text-red-400 hover:text-white">
            <X className="w-4 h-4" />
          </button>
        </div>
      )}

      {/* Filter / Search Bar */}
      <div className="p-4 bg-[#161724] border-2 border-black shadow-[4px_4px_0px_#000] flex flex-col md:flex-row gap-4">
        {/* Search Input */}
        <div className="relative flex-1">
          <Search className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search events by title, slug, or keywords..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full bg-[#0e0f19] border-2 border-black text-white pl-10 pr-3 py-2 text-xs font-mono shadow-[inset_2px_2px_4px_rgba(0,0,0,0.5)] focus:border-[#a46bff] outline-none"
          />
        </div>

        {/* Category Dropdown */}
        <div className="flex items-center gap-2">
          <Filter className="w-4 h-4 text-neutral-400 shrink-0" />
          <select
            value={categoryFilter}
            onChange={(e) =>
              setCategoryFilter(
                e.target.value === "ALL" ? "ALL" : Number(e.target.value)
              )
            }
            className="bg-[#0e0f19] border-2 border-black text-neutral-200 px-3 py-2 text-xs font-mono outline-none cursor-pointer"
          >
            <option value="ALL">All Categories / Biomes</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>
        </div>

        {/* Status Dropdown */}
        <div>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value as any)}
            className="bg-[#0e0f19] border-2 border-black text-neutral-200 px-3 py-2 text-xs font-mono outline-none cursor-pointer w-full md:w-auto"
          >
            <option value="ALL">All Statuses</option>
            <option value="PUBLISHED">Published Live Only</option>
            <option value="DRAFT">Unpublished Drafts Only</option>
          </select>
        </div>
      </div>

      {/* Events Table */}
      <div className="bg-[#161724] border-2 border-black shadow-[4px_4px_0px_#000] overflow-x-auto">
        <table className="w-full text-left border-collapse text-xs font-mono">
          <thead>
            <tr className="bg-[#1e2033] border-b-2 border-black text-neutral-300 font-pixel tracking-wider uppercase">
              <th className="p-3">Event Title &amp; Slug</th>
              <th className="p-3">Biome Domain</th>
              <th className="p-3">Format</th>
              <th className="p-3">Venue</th>
              <th className="p-3">Sessions</th>
              <th className="p-3">Status</th>
              <th className="p-3 text-right">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-black/40">
            {loading ? (
              <tr>
                <td colSpan={7} className="p-8 text-center text-neutral-400">
                  <Loader2 className="w-6 h-6 animate-spin mx-auto mb-2 text-[#a46bff]" />
                  <span>Loading festival catalog...</span>
                </td>
              </tr>
            ) : filteredEvents.length === 0 ? (
              <tr>
                <td colSpan={7} className="p-8 text-center text-neutral-400">
                  No matching events found. Create your first event or adjust filters!
                </td>
              </tr>
            ) : (
              filteredEvents.map((ev) => (
                <tr key={ev.id} className="hover:bg-[#1a1c2b] transition">
                  <td className="p-3">
                    <div className="font-bold text-white text-sm">{ev.title}</div>
                    <div className="text-[11px] text-neutral-400 font-mono">{ev.slug}</div>
                  </td>
                  <td className="p-3">
                    <span
                      className="inline-flex items-center gap-1.5 px-2 py-0.5 border border-black text-[11px]"
                      style={{
                        backgroundColor: `${ev.categoryColor}22`,
                        color: ev.categoryColor,
                        borderColor: ev.categoryColor,
                      }}
                    >
                      {ev.categoryName}
                    </span>
                  </td>
                  <td className="p-3">
                    <div>{ev.participation}</div>
                    <div className="text-[10px] text-neutral-400">{ev.teamSize}</div>
                  </td>
                  <td className="p-3 text-neutral-300">{ev.venue}</td>
                  <td className="p-3">
                    <span className="px-2 py-0.5 bg-[#0d0e15] border border-black text-[11px]">
                      {ev.sessionCount} {ev.sessionCount === 1 ? "slot" : "slots"}
                    </span>
                  </td>
                  <td className="p-3">
                    {ev.isPublished ? (
                      <span className="inline-flex items-center gap-1 px-2 py-0.5 bg-emerald-950 border border-emerald-700 text-emerald-300 text-[10px] font-pixel uppercase tracking-wide">
                        <CheckCircle className="w-3 h-3" />
                        <span>Live</span>
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1 px-2 py-0.5 bg-amber-950 border border-amber-700 text-amber-300 text-[10px] font-pixel uppercase tracking-wide">
                        <Clock className="w-3 h-3" />
                        <span>Draft</span>
                      </span>
                    )}
                  </td>
                  <td className="p-3 text-right">
                    <div className="flex items-center justify-end gap-1.5">
                      {/* Toggle Publish */}
                      <button
                        onClick={() => handleTogglePublish(ev.id)}
                        title={ev.isPublished ? "Unpublish event" : "Publish event"}
                        className="p-1.5 bg-[#1b1d2d] hover:bg-[#25283f] border border-black text-neutral-300 active:translate-y-0.5 cursor-pointer"
                      >
                        {ev.isPublished ? (
                          <EyeOff className="w-4 h-4 text-amber-400" />
                        ) : (
                          <Eye className="w-4 h-4 text-emerald-400" />
                        )}
                      </button>

                      {/* Edit */}
                      <button
                        onClick={() => handleOpenEdit(ev.id)}
                        title="Edit event"
                        className="p-1.5 bg-[#1b1d2d] hover:bg-[#25283f] border border-black text-neutral-300 active:translate-y-0.5 cursor-pointer"
                      >
                        <Edit2 className="w-4 h-4 text-sky-400" />
                      </button>

                      {/* Delete */}
                      <button
                        onClick={() => setDeleteConfirmId(ev.id)}
                        title="Delete event"
                        className="p-1.5 bg-[#1b1d2d] hover:bg-[#3d1b1b] border border-black text-neutral-300 active:translate-y-0.5 cursor-pointer"
                      >
                        <Trash2 className="w-4 h-4 text-red-400" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* CREATE / EDIT MODAL */}
      {modalOpen && (
        <div className="fixed inset-0 z-50 bg-black/80 flex items-center justify-center p-4 backdrop-blur-sm overflow-y-auto">
          <div className="bg-[#161724] border-4 border-black shadow-[8px_8px_0px_#000] w-full max-w-3xl my-8 max-h-[90vh] flex flex-col">
            {/* Modal Header */}
            <div className="p-4 bg-[#1f2033] border-b-2 border-black flex items-center justify-between">
              <h2 className="font-pixel text-lg text-gold uppercase tracking-wider">
                {editingEventId ? "Edit Festival Event" : "Create New Festival Event"}
              </h2>
              <button
                onClick={() => setModalOpen(false)}
                className="text-neutral-400 hover:text-white"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Modal Form */}
            <form onSubmit={handleSubmitEvent} className="p-6 space-y-4 overflow-y-auto flex-1">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {/* Title */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Event Title *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.title}
                    onChange={(e) => handleTitleChange(e.target.value)}
                    placeholder="e.g. Speed Debugging Clash"
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none focus:border-[#a46bff]"
                  />
                </div>

                {/* Slug */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Unique Slug *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.slug}
                    onChange={(e) =>
                      setFormData({ ...formData, slug: e.target.value.toLowerCase() })
                    }
                    placeholder="e.g. speed-debugging-clash"
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none focus:border-[#a46bff]"
                  />
                </div>

                {/* Category */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Biome Domain / Category *
                  </label>
                  <select
                    required
                    value={formData.categoryId}
                    onChange={(e) =>
                      setFormData({ ...formData, categoryId: Number(e.target.value) })
                    }
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  >
                    {categories.map((c) => (
                      <option key={c.id} value={c.id}>
                        {c.name} ({c.code})
                      </option>
                    ))}
                  </select>
                </div>

                {/* Participation Format */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Participation Format *
                  </label>
                  <select
                    value={formData.participation}
                    onChange={(e) =>
                      setFormData({ ...formData, participation: e.target.value as any })
                    }
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  >
                    <option value="Individual">Individual</option>
                    <option value="Team">Team</option>
                    <option value="Individual or Team">Individual or Team</option>
                  </select>
                </div>

                {/* Team Size */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Team Size
                  </label>
                  <input
                    type="text"
                    value={formData.teamSize || ""}
                    onChange={(e) => setFormData({ ...formData, teamSize: e.target.value })}
                    placeholder="e.g. 2 - 4 members"
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  />
                </div>

                {/* Duration */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Duration
                  </label>
                  <input
                    type="text"
                    value={formData.duration || ""}
                    onChange={(e) => setFormData({ ...formData, duration: e.target.value })}
                    placeholder="e.g. 3 hours / 24 hours"
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  />
                </div>

                {/* Venue */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Main Venue *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.venue}
                    onChange={(e) => setFormData({ ...formData, venue: e.target.value })}
                    placeholder="e.g. Computer Lab 2, Academic Block"
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  />
                </div>

                {/* Prize */}
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Prize Pool
                  </label>
                  <input
                    type="text"
                    value={formData.prize || ""}
                    onChange={(e) => setFormData({ ...formData, prize: e.target.value })}
                    placeholder="e.g. ₹20,000 + Tech Swag"
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  />
                </div>
              </div>

              {/* Summary */}
              <div>
                <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                  Card Summary (1-2 sentences) *
                </label>
                <input
                  type="text"
                  required
                  maxLength={300}
                  value={formData.summary}
                  onChange={(e) => setFormData({ ...formData, summary: e.target.value })}
                  placeholder="Shown on the festival event cards"
                  className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                />
              </div>

              {/* Description */}
              <div>
                <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                  Detailed Description *
                </label>
                <textarea
                  required
                  rows={4}
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder="Complete rules, judging criteria, and requirements"
                  className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                />
              </div>

              {/* Registration URL & Published Toggle */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Registration URL
                  </label>
                  <input
                    type="url"
                    value={formData.registrationUrl || ""}
                    onChange={(e) =>
                      setFormData({ ...formData, registrationUrl: e.target.value })
                    }
                    placeholder="https://unstop.com/..."
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  />
                </div>

                <div className="flex items-center gap-3 pt-5">
                  <input
                    type="checkbox"
                    id="isPublished"
                    checked={formData.isPublished}
                    onChange={(e) =>
                      setFormData({ ...formData, isPublished: e.target.checked })
                    }
                    className="w-4 h-4 cursor-pointer"
                  />
                  <label
                    htmlFor="isPublished"
                    className="text-xs font-pixel text-neutral-200 uppercase cursor-pointer"
                  >
                    Publish immediately to public website
                  </label>
                </div>
              </div>

              {/* SUB-FORM: Timetable Sessions */}
              <div className="mt-4 pt-4 border-t-2 border-black">
                <div className="flex items-center justify-between mb-3">
                  <h3 className="font-pixel text-sm text-gold uppercase tracking-wide">
                    Event Schedule Slots / Timetable (Relational Sessions)
                  </h3>
                  <button
                    type="button"
                    onClick={handleAddSession}
                    className="px-2.5 py-1 bg-[#25283d] hover:bg-[#343854] text-neutral-200 border border-black text-[11px] font-mono flex items-center gap-1"
                  >
                    <Plus className="w-3.5 h-3.5" />
                    <span>Add Slot</span>
                  </button>
                </div>

                <div className="space-y-3">
                  {formData.sessions?.map((sess, idx) => (
                    <div
                      key={idx}
                      className="p-3 bg-[#0e0f19] border border-black flex flex-col md:flex-row gap-3 items-start md:items-center"
                    >
                      <div className="w-20">
                        <label className="block text-[10px] text-neutral-400 font-mono">
                          Day
                        </label>
                        <select
                          value={sess.festDay}
                          onChange={(e) => {
                            const val = Number(e.target.value);
                            setFormData((prev) => {
                              const list = [...(prev.sessions || [])];
                              list[idx].festDay = val;
                              return { ...prev, sessions: list };
                            });
                          }}
                          className="bg-black border border-neutral-700 text-white px-2 py-1 text-xs font-mono w-full"
                        >
                          <option value={1}>Day 1</option>
                          <option value={2}>Day 2</option>
                          <option value={3}>Day 3</option>
                        </select>
                      </div>

                      <div className="w-28">
                        <label className="block text-[10px] text-neutral-400 font-mono">
                          Time Window
                        </label>
                        <input
                          type="text"
                          value={sess.startTime}
                          onChange={(e) => {
                            const val = e.target.value;
                            setFormData((prev) => {
                              const list = [...(prev.sessions || [])];
                              list[idx].startTime = val;
                              return { ...prev, sessions: list };
                            });
                          }}
                          placeholder="10:00 AM"
                          className="bg-black border border-neutral-700 text-white px-2 py-1 text-xs font-mono w-full"
                        />
                      </div>

                      <div className="flex-1">
                        <label className="block text-[10px] text-neutral-400 font-mono">
                          Session Title
                        </label>
                        <input
                          type="text"
                          value={sess.sessionTitle}
                          onChange={(e) => {
                            const val = e.target.value;
                            setFormData((prev) => {
                              const list = [...(prev.sessions || [])];
                              list[idx].sessionTitle = val;
                              return { ...prev, sessions: list };
                            });
                          }}
                          placeholder="e.g. Round 1 Elimination"
                          className="bg-black border border-neutral-700 text-white px-2 py-1 text-xs font-mono w-full"
                        />
                      </div>

                      <div className="w-36">
                        <label className="block text-[10px] text-neutral-400 font-mono">
                          Session Venue
                        </label>
                        <input
                          type="text"
                          value={sess.venue || ""}
                          onChange={(e) => {
                            const val = e.target.value;
                            setFormData((prev) => {
                              const list = [...(prev.sessions || [])];
                              list[idx].venue = val;
                              return { ...prev, sessions: list };
                            });
                          }}
                          placeholder="Venue"
                          className="bg-black border border-neutral-700 text-white px-2 py-1 text-xs font-mono w-full"
                        />
                      </div>

                      <button
                        type="button"
                        onClick={() => handleRemoveSession(idx)}
                        className="mt-4 p-1 text-red-400 hover:text-red-300"
                        title="Remove session"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  ))}
                </div>
              </div>

              {/* Modal Buttons */}
              <div className="pt-4 border-t-2 border-black flex items-center justify-end gap-3">
                <button
                  type="button"
                  onClick={() => setModalOpen(false)}
                  className="px-4 py-2 bg-[#25283d] hover:bg-[#343854] text-neutral-300 font-pixel text-xs tracking-wider border-2 border-black"
                >
                  CANCEL
                </button>
                <button
                  type="submit"
                  disabled={saving}
                  className="px-5 py-2 bg-[#15803d] hover:bg-[#16a34a] text-white font-pixel text-xs tracking-wider border-2 border-black shadow-[3px_3px_0px_#000] flex items-center gap-2 cursor-pointer"
                >
                  {saving ? (
                    <Loader2 className="w-4 h-4 animate-spin" />
                  ) : (
                    <Save className="w-4 h-4" />
                  )}
                  <span>{editingEventId ? "SAVE CHANGES" : "CREATE EVENT"}</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* DELETE CONFIRMATION MODAL */}
      {deleteConfirmId && (
        <div className="fixed inset-0 z-50 bg-black/80 flex items-center justify-center p-4 backdrop-blur-sm">
          <div className="bg-[#161724] border-4 border-black shadow-[8px_8px_0px_#000] max-w-md w-full p-6 text-center space-y-4">
            <div className="w-12 h-12 bg-red-950 border-2 border-red-600 text-red-400 flex items-center justify-center mx-auto shadow-[3px_3px_0px_#000]">
              <Trash2 className="w-6 h-6" />
            </div>
            <h3 className="font-pixel text-lg text-white uppercase tracking-wider">
              Confirm Event Deletion
            </h3>
            <p className="text-xs text-neutral-300 font-mono">
              Are you sure you want to permanently delete this event? This action will cascade
              delete all scheduled session slots and log an automated trigger audit record.
            </p>
            <div className="pt-2 flex items-center justify-center gap-3">
              <button
                onClick={() => setDeleteConfirmId(null)}
                className="px-4 py-2 bg-[#25283d] hover:bg-[#343854] text-neutral-300 font-pixel text-xs tracking-wider border-2 border-black"
              >
                CANCEL
              </button>
              <button
                onClick={handleDeleteConfirm}
                className="px-4 py-2 bg-[#991b1b] hover:bg-[#b91c1c] text-white font-pixel text-xs tracking-wider border-2 border-black shadow-[3px_3px_0px_#000]"
              >
                CONFIRM DELETE
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
