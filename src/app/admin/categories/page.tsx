"use client";

import React, { useEffect, useState } from "react";
import {
  Layers,
  Plus,
  Edit2,
  Trash2,
  CheckCircle,
  AlertCircle,
  X,
  Loader2,
  Save,
  Tag,
  Palette,
} from "lucide-react";
import { api, Category, CategoryFormData } from "@/lib/api";

export default function AdminCategoriesPage() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  // Modal State
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [saving, setSaving] = useState(false);

  const initialForm: CategoryFormData = {
    code: "",
    name: "",
    description: "",
    colorHex: "#a46bff",
    iconName: "Terminal",
    displayOrder: 1,
    isActive: true,
  };

  const [formData, setFormData] = useState<CategoryFormData>(initialForm);

  const fetchCategories = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.getAdminCategories();
      setCategories(data);
    } catch (err: any) {
      setError(err?.message || "Failed to load categories");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleOpenCreate = () => {
    setEditingId(null);
    setFormData(initialForm);
    setModalOpen(true);
  };

  const handleOpenEdit = (cat: Category) => {
    setEditingId(cat.id);
    setFormData({
      code: cat.code,
      name: cat.name,
      description: cat.description || "",
      colorHex: cat.colorHex,
      iconName: cat.iconName,
      displayOrder: cat.displayOrder,
      isActive: cat.isActive,
    });
    setModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      if (editingId) {
        await api.updateCategory(editingId, formData);
        setSuccess(`Updated category "${formData.name}"`);
      } else {
        await api.createCategory(formData);
        setSuccess(`Created category "${formData.name}"`);
      }
      setModalOpen(false);
      fetchCategories();
    } catch (err: any) {
      setError(err?.message || "Failed to save category");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id: number, name: string) => {
    if (!confirm(`Are you sure you want to delete category "${name}"?`)) return;
    try {
      await api.deleteCategory(id);
      setSuccess(`Deleted category "${name}"`);
      fetchCategories();
    } catch (err: any) {
      setError(err?.message || "Failed to delete category");
    }
  };

  return (
    <div className="space-y-6">
      {/* Header bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b-2 border-black">
        <div>
          <h1 className="font-pixel text-2xl sm:text-3xl text-gold tracking-wide uppercase">
            Biome Domains &amp; Categories
          </h1>
          <p className="text-xs text-neutral-400 font-mono mt-1">
            Relational 3NF Category Catalog &bull; Foreign Key Targets for Events
          </p>
        </div>

        <button
          onClick={handleOpenCreate}
          className="flex items-center gap-2 px-4 py-2.5 bg-[#15803d] hover:bg-[#16a34a] text-white border-2 border-black shadow-[3px_3px_0px_#000] font-pixel text-xs tracking-wider uppercase active:translate-y-0.5 transition cursor-pointer"
        >
          <Plus className="w-4 h-4" />
          <span>Add Domain Category</span>
        </button>
      </div>

      {/* Notifications */}
      {success && (
        <div className="p-3 bg-[#064e3b] border-2 border-[#059669] text-emerald-200 text-xs font-mono flex items-center justify-between shadow-[2px_2px_0px_#000]">
          <div className="flex items-center gap-2">
            <CheckCircle className="w-4 h-4 text-emerald-300" />
            <span>{success}</span>
          </div>
          <button onClick={() => setSuccess(null)} className="text-emerald-400 hover:text-white">
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

      {/* Categories Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {loading ? (
          <div className="col-span-full py-16 text-center text-neutral-400 font-mono text-xs">
            <Loader2 className="w-8 h-8 animate-spin mx-auto mb-2 text-[#a46bff]" />
            <span>Loading categories...</span>
          </div>
        ) : categories.length === 0 ? (
          <div className="col-span-full py-16 text-center text-neutral-400 font-mono text-xs">
            No categories defined in the database.
          </div>
        ) : (
          categories.map((cat) => (
            <div
              key={cat.id}
              className="bg-[#161724] border-2 border-black p-5 shadow-[4px_4px_0px_#000] flex flex-col justify-between relative overflow-hidden"
            >
              {/* Top Accent Strip */}
              <div
                className="absolute top-0 left-0 right-0 h-1.5"
                style={{ backgroundColor: cat.colorHex }}
              />

              <div>
                <div className="flex items-start justify-between gap-2 mb-2">
                  <div className="flex items-center gap-2">
                    <span
                      className="w-4 h-4 border border-black shrink-0"
                      style={{ backgroundColor: cat.colorHex }}
                    />
                    <h3 className="font-bold text-white text-sm">{cat.name}</h3>
                  </div>
                  <span className="text-[10px] font-mono px-1.5 py-0.5 bg-[#0e0f19] border border-black text-neutral-400">
                    {cat.code}
                  </span>
                </div>

                <p className="text-xs text-neutral-300 font-mono line-clamp-2 mb-4">
                  {cat.description || "No description provided."}
                </p>
              </div>

              <div className="pt-3 border-t border-black/40 flex items-center justify-between text-xs font-mono">
                <span className="text-neutral-400 text-[11px]">
                  Events: <strong className="text-gold">{cat.eventCount ?? 0}</strong>
                </span>

                <div className="flex items-center gap-2">
                  <button
                    onClick={() => handleOpenEdit(cat)}
                    className="p-1.5 bg-[#1f2133] hover:bg-[#2b2e46] border border-black text-sky-400 cursor-pointer"
                    title="Edit category"
                  >
                    <Edit2 className="w-3.5 h-3.5" />
                  </button>
                  <button
                    onClick={() => handleDelete(cat.id, cat.name)}
                    className="p-1.5 bg-[#1f2133] hover:bg-[#3d1b1b] border border-black text-red-400 cursor-pointer"
                    title="Delete category"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {/* CREATE / EDIT CATEGORY MODAL */}
      {modalOpen && (
        <div className="fixed inset-0 z-50 bg-black/80 flex items-center justify-center p-4 backdrop-blur-sm">
          <div className="bg-[#161724] border-4 border-black shadow-[8px_8px_0px_#000] w-full max-w-lg p-6 space-y-4">
            <div className="flex items-center justify-between pb-3 border-b-2 border-black">
              <h2 className="font-pixel text-lg text-gold uppercase tracking-wider">
                {editingId ? "Edit Domain Category" : "Add Domain Category"}
              </h2>
              <button
                onClick={() => setModalOpen(false)}
                className="text-neutral-400 hover:text-white"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                  Category Code (Slug / Unique) *
                </label>
                <input
                  type="text"
                  required
                  disabled={!!editingId}
                  value={formData.code}
                  onChange={(e) =>
                    setFormData({ ...formData, code: e.target.value.toLowerCase().trim() })
                  }
                  placeholder="e.g. quantum-computing"
                  className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none disabled:opacity-50"
                />
              </div>

              <div>
                <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                  Display Name *
                </label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g. Quantum Realm"
                  className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                  Description
                </label>
                <textarea
                  rows={3}
                  value={formData.description || ""}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder="Brief description of the events belonging to this biome"
                  className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Theme Hex Color *
                  </label>
                  <div className="flex items-center gap-2">
                    <input
                      type="color"
                      value={formData.colorHex}
                      onChange={(e) => setFormData({ ...formData, colorHex: e.target.value })}
                      className="w-8 h-8 border border-black cursor-pointer bg-transparent"
                    />
                    <input
                      type="text"
                      required
                      pattern="^#[0-9A-Fa-f]{6}$"
                      value={formData.colorHex}
                      onChange={(e) => setFormData({ ...formData, colorHex: e.target.value })}
                      className="flex-1 bg-[#0d0e15] border-2 border-black text-white px-3 py-1.5 text-xs font-mono outline-none"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-pixel text-neutral-300 uppercase mb-1">
                    Display Order
                  </label>
                  <input
                    type="number"
                    value={formData.displayOrder}
                    onChange={(e) =>
                      setFormData({ ...formData, displayOrder: Number(e.target.value) })
                    }
                    className="w-full bg-[#0d0e15] border-2 border-black text-white px-3 py-2 text-xs font-mono outline-none"
                  />
                </div>
              </div>

              <div className="pt-3 border-t-2 border-black flex items-center justify-end gap-3">
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
                  <span>SAVE CATEGORY</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
