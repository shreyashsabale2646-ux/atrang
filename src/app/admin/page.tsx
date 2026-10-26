"use client";

import React, { useEffect, useState } from "react";
import Link from "next/link";
import {
  Calendar,
  Layers,
  CheckCircle2,
  Clock,
  Plus,
  RefreshCw,
  AlertCircle,
  Activity,
  ArrowRight,
  TrendingUp,
} from "lucide-react";
import { api, DashboardSummary, AuditLog } from "@/lib/api";

export default function AdminDashboardPage() {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [recentLogs, setRecentLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [sumData, logsData] = await Promise.all([
        api.getDashboardSummary(),
        api.getAuditLogs(0, 6),
      ]);
      setSummary(sumData);
      setRecentLogs(logsData.content || []);
    } catch (err: any) {
      setError(err?.message || "Failed to load dashboard metrics");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="space-y-6">
      {/* Header bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b-2 border-black">
        <div>
          <h1 className="font-pixel text-2xl sm:text-3xl text-gold tracking-wide uppercase">
            Festival Control Panel
          </h1>
          <p className="text-xs text-neutral-400 font-mono mt-1">
            Real-time PostgreSQL Relational Database Metrics &amp; Audit Logs
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={fetchData}
            disabled={loading}
            className="flex items-center gap-2 px-3 py-2 bg-[#1f2030] hover:bg-[#282a3f] text-neutral-200 border-2 border-black shadow-[2px_2px_0px_#000] text-xs font-mono active:translate-y-0.5 cursor-pointer"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${loading ? "animate-spin" : ""}`} />
            <span>Refresh</span>
          </button>
          <Link
            href="/admin/events"
            className="flex items-center gap-2 px-4 py-2 bg-[#15803d] hover:bg-[#16a34a] text-white border-2 border-black shadow-[3px_3px_0px_#000] font-pixel text-xs tracking-wider uppercase active:translate-y-0.5"
          >
            <Plus className="w-4 h-4" />
            <span>Manage Events</span>
          </Link>
        </div>
      </div>

      {error && (
        <div className="p-4 bg-[#450a0a] border-2 border-[#b91c1c] text-red-200 font-mono text-xs flex items-center justify-between shadow-[3px_3px_0px_#000]">
          <div className="flex items-center gap-2">
            <AlertCircle className="w-4 h-4 text-red-400 shrink-0" />
            <span>{error}</span>
          </div>
          <button onClick={fetchData} className="underline text-red-300 hover:text-white">
            Retry
          </button>
        </div>
      )}

      {/* Metric Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Events */}
        <div className="bg-[#161724] border-2 border-black p-5 shadow-[4px_4px_0px_#000] relative overflow-hidden">
          <div className="flex items-center justify-between">
            <span className="font-pixel text-xs text-neutral-400 uppercase tracking-wider">
              Total Events
            </span>
            <div className="p-2 bg-[#212338] border border-black text-[#a46bff]">
              <Calendar className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3 font-pixel text-3xl sm:text-4xl text-white tracking-wide">
            {loading ? "..." : summary?.totalEvents ?? 0}
          </div>
          <div className="mt-2 text-[11px] font-mono text-neutral-400 flex items-center gap-1">
            <TrendingUp className="w-3.5 h-3.5 text-emerald-400" />
            <span>Full relational catalogue</span>
          </div>
        </div>

        {/* Published Events */}
        <div className="bg-[#161724] border-2 border-black p-5 shadow-[4px_4px_0px_#000]">
          <div className="flex items-center justify-between">
            <span className="font-pixel text-xs text-neutral-400 uppercase tracking-wider">
              Published Live
            </span>
            <div className="p-2 bg-[#143324] border border-black text-emerald-400">
              <CheckCircle2 className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3 font-pixel text-3xl sm:text-4xl text-emerald-400 tracking-wide">
            {loading ? "..." : summary?.publishedEvents ?? 0}
          </div>
          <div className="mt-2 text-[11px] font-mono text-neutral-400">
            Visible on public website
          </div>
        </div>

        {/* Draft Events */}
        <div className="bg-[#161724] border-2 border-black p-5 shadow-[4px_4px_0px_#000]">
          <div className="flex items-center justify-between">
            <span className="font-pixel text-xs text-neutral-400 uppercase tracking-wider">
              Unpublished Drafts
            </span>
            <div className="p-2 bg-[#332614] border border-black text-amber-400">
              <Clock className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3 font-pixel text-3xl sm:text-4xl text-amber-400 tracking-wide">
            {loading ? "..." : summary?.draftEvents ?? 0}
          </div>
          <div className="mt-2 text-[11px] font-mono text-neutral-400">
            Pending coordinator review
          </div>
        </div>

        {/* Event Categories */}
        <div className="bg-[#161724] border-2 border-black p-5 shadow-[4px_4px_0px_#000]">
          <div className="flex items-center justify-between">
            <span className="font-pixel text-xs text-neutral-400 uppercase tracking-wider">
              Biomes / Categories
            </span>
            <div className="p-2 bg-[#2d1b38] border border-black text-[#e879f9]">
              <Layers className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-3 font-pixel text-3xl sm:text-4xl text-[#e879f9] tracking-wide">
            {loading ? "..." : summary?.totalCategories ?? 0}
          </div>
          <div className="mt-2 text-[11px] font-mono text-neutral-400">
            Technical fest domains
          </div>
        </div>
      </div>

      {/* Main Grid: Category Distribution & Recent Audit Logs */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left: Category Breakdown Bars */}
        <div className="lg:col-span-2 bg-[#161724] border-2 border-black p-6 shadow-[4px_4px_0px_#000]">
          <div className="flex items-center justify-between pb-4 mb-4 border-b-2 border-black">
            <div>
              <h2 className="font-pixel text-lg text-gold tracking-wide uppercase">
                Events by Technical Domain
              </h2>
              <p className="text-xs text-neutral-400 font-mono">
                PostgreSQL Aggregate Query: COUNT(*) GROUP BY category_id
              </p>
            </div>
            <Link
              href="/admin/categories"
              className="text-xs font-mono text-[#a46bff] hover:text-[#c084fc] flex items-center gap-1"
            >
              <span>Manage</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          {loading ? (
            <div className="py-12 text-center text-neutral-400 font-mono text-xs">
              Calculating domain statistics...
            </div>
          ) : !summary?.categoryMetrics || summary.categoryMetrics.length === 0 ? (
            <div className="py-12 text-center text-neutral-400 font-mono text-xs">
              No category data recorded in PostgreSQL.
            </div>
          ) : (
            <div className="space-y-4">
              {summary.categoryMetrics.map((cat) => {
                const total = cat.totalEvents;
                const publishedPct = total > 0 ? (cat.publishedEvents / total) * 100 : 0;
                return (
                  <div key={cat.categoryId} className="space-y-1.5">
                    <div className="flex items-center justify-between text-xs font-mono">
                      <div className="flex items-center gap-2">
                        <span
                          className="w-3 h-3 border border-black"
                          style={{ backgroundColor: cat.colorHex }}
                        />
                        <span className="font-semibold text-neutral-200">{cat.categoryName}</span>
                      </div>
                      <div className="text-neutral-400 text-[11px]">
                        <span className="text-emerald-400 font-bold">{cat.publishedEvents}</span>{" "}
                        pub /{" "}
                        <span className="text-amber-400 font-bold">{cat.draftEvents}</span> draft (
                        {total} total)
                      </div>
                    </div>

                    {/* Progress Bar */}
                    <div className="w-full h-3 bg-[#0d0e15] border border-black overflow-hidden flex">
                      <div
                        className="h-full bg-emerald-500 transition-all duration-500"
                        style={{ width: `${publishedPct}%` }}
                        title={`${cat.publishedEvents} Published`}
                      />
                      <div
                        className="h-full bg-amber-500/80 transition-all duration-500"
                        style={{ width: `${100 - publishedPct}%` }}
                        title={`${cat.draftEvents} Drafts`}
                      />
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Right: Recent Audit Activity */}
        <div className="bg-[#161724] border-2 border-black p-6 shadow-[4px_4px_0px_#000] flex flex-col">
          <div className="flex items-center justify-between pb-4 mb-4 border-b-2 border-black">
            <div className="flex items-center gap-2">
              <Activity className="w-4 h-4 text-emerald-400" />
              <h2 className="font-pixel text-lg text-gold tracking-wide uppercase">
                Trigger Audit Log
              </h2>
            </div>
            <Link
              href="/admin/audit-logs"
              className="text-xs font-mono text-[#a46bff] hover:text-[#c084fc] flex items-center gap-1"
            >
              <span>View All</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          <div className="flex-1 space-y-3 overflow-y-auto max-h-[360px]">
            {loading ? (
              <div className="py-12 text-center text-neutral-400 font-mono text-xs">
                Loading audit trail...
              </div>
            ) : recentLogs.length === 0 ? (
              <div className="py-12 text-center text-neutral-400 font-mono text-xs">
                No audit entries recorded yet.
              </div>
            ) : (
              recentLogs.map((log) => {
                const actionColor =
                  log.action === "CREATE"
                    ? "bg-emerald-950 text-emerald-300 border-emerald-800"
                    : log.action === "PUBLISH"
                    ? "bg-blue-950 text-blue-300 border-blue-800"
                    : log.action === "DELETE"
                    ? "bg-rose-950 text-rose-300 border-rose-800"
                    : "bg-amber-950 text-amber-300 border-amber-800";

                return (
                  <div
                    key={log.id}
                    className="p-2.5 bg-[#0e0f19] border border-black hover:border-neutral-700 transition"
                  >
                    <div className="flex items-center justify-between mb-1">
                      <span
                        className={`px-1.5 py-0.5 text-[9px] font-pixel border uppercase ${actionColor}`}
                      >
                        {log.action}
                      </span>
                      <span className="text-[10px] font-mono text-neutral-400">
                        {log.performedBy}
                      </span>
                    </div>
                    <p className="text-[11px] font-mono text-neutral-300 line-clamp-2">
                      {log.details}
                    </p>
                  </div>
                );
              })
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
