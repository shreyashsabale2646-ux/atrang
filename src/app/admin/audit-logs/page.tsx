"use client";

import React, { useEffect, useState } from "react";
import {
  FileText,
  RefreshCw,
  AlertCircle,
  ShieldAlert,
  ChevronLeft,
  ChevronRight,
  Loader2,
} from "lucide-react";
import { api, AuditLog, PageResponse } from "@/lib/api";

export default function AdminAuditLogsPage() {
  const [logsPage, setLogsPage] = useState<PageResponse<AuditLog> | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchLogs = async (page = 0) => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.getAuditLogs(page, 15);
      setLogsPage(data);
      setCurrentPage(page);
    } catch (err: any) {
      setError(err?.message || "Failed to load audit logs");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLogs(0);
  }, []);

  return (
    <div className="space-y-6">
      {/* Header bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b-2 border-black">
        <div>
          <h1 className="font-pixel text-2xl sm:text-3xl text-gold tracking-wide uppercase">
            Security &amp; Trigger Audit Logs
          </h1>
          <p className="text-xs text-neutral-400 font-mono mt-1">
            Automated PostgreSQL Trigger Logs &bull; Relational Audit Trail
          </p>
        </div>

        <button
          onClick={() => fetchLogs(currentPage)}
          disabled={loading}
          className="flex items-center gap-2 px-3 py-2 bg-[#1f2030] hover:bg-[#282a3f] text-neutral-200 border-2 border-black shadow-[2px_2px_0px_#000] text-xs font-mono active:translate-y-0.5 cursor-pointer self-start sm:self-auto"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${loading ? "animate-spin" : ""}`} />
          <span>Refresh Logs</span>
        </button>
      </div>

      {error && (
        <div className="p-3 bg-[#450a0a] border-2 border-[#b91c1c] text-red-200 text-xs font-mono flex items-center justify-between shadow-[2px_2px_0px_#000]">
          <div className="flex items-center gap-2">
            <AlertCircle className="w-4 h-4 text-red-300 shrink-0" />
            <span>{error}</span>
          </div>
          <button
            onClick={() => fetchLogs(currentPage)}
            className="underline text-red-300 hover:text-white"
          >
            Retry
          </button>
        </div>
      )}

      {/* Audit Log Table */}
      <div className="bg-[#161724] border-2 border-black shadow-[4px_4px_0px_#000] overflow-x-auto">
        <table className="w-full text-left border-collapse text-xs font-mono">
          <thead>
            <tr className="bg-[#1e2033] border-b-2 border-black text-neutral-300 font-pixel tracking-wider uppercase">
              <th className="p-3">ID</th>
              <th className="p-3">Action</th>
              <th className="p-3">Event ID</th>
              <th className="p-3">Actor / Origin</th>
              <th className="p-3">Change Details</th>
              <th className="p-3 text-right">Timestamp</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-black/40">
            {loading ? (
              <tr>
                <td colSpan={6} className="p-8 text-center text-neutral-400">
                  <Loader2 className="w-6 h-6 animate-spin mx-auto mb-2 text-[#a46bff]" />
                  <span>Loading audit logs...</span>
                </td>
              </tr>
            ) : !logsPage?.content || logsPage.content.length === 0 ? (
              <tr>
                <td colSpan={6} className="p-8 text-center text-neutral-400">
                  No audit log entries recorded in PostgreSQL.
                </td>
              </tr>
            ) : (
              logsPage.content.map((log) => {
                const actionColor =
                  log.action === "CREATE"
                    ? "bg-emerald-950 text-emerald-300 border-emerald-800"
                    : log.action === "PUBLISH"
                    ? "bg-blue-950 text-blue-300 border-blue-800"
                    : log.action === "DELETE"
                    ? "bg-rose-950 text-rose-300 border-rose-800"
                    : log.action === "LOGIN"
                    ? "bg-purple-950 text-purple-300 border-purple-800"
                    : "bg-amber-950 text-amber-300 border-amber-800";

                return (
                  <tr key={log.id} className="hover:bg-[#1a1c2b] transition">
                    <td className="p-3 text-neutral-400 font-mono">#{log.id}</td>
                    <td className="p-3">
                      <span
                        className={`inline-block px-2 py-0.5 text-[10px] font-pixel border uppercase ${actionColor}`}
                      >
                        {log.action}
                      </span>
                    </td>
                    <td className="p-3 text-neutral-300">
                      {log.eventId ? `#${log.eventId}` : "-"}
                    </td>
                    <td className="p-3">
                      <div className="font-semibold text-neutral-200">{log.performedBy}</div>
                      {log.ipAddress && (
                        <div className="text-[10px] text-neutral-500">{log.ipAddress}</div>
                      )}
                    </td>
                    <td className="p-3 text-neutral-300 max-w-md break-words">{log.details}</td>
                    <td className="p-3 text-right text-neutral-400 text-[11px] whitespace-nowrap">
                      {new Date(log.createdAt).toLocaleString()}
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>

        {/* Pagination Footer */}
        {logsPage && logsPage.totalPages > 1 && (
          <div className="p-3 bg-[#131420] border-t-2 border-black flex items-center justify-between text-xs font-mono">
            <span className="text-neutral-400">
              Page {logsPage.number + 1} of {logsPage.totalPages} ({logsPage.totalElements} records)
            </span>
            <div className="flex items-center gap-2">
              <button
                disabled={currentPage === 0 || loading}
                onClick={() => fetchLogs(currentPage - 1)}
                className="px-2.5 py-1 bg-[#202235] hover:bg-[#2b2e47] disabled:opacity-40 border border-black text-neutral-200 flex items-center gap-1 cursor-pointer"
              >
                <ChevronLeft className="w-3.5 h-3.5" />
                <span>Prev</span>
              </button>
              <button
                disabled={currentPage >= logsPage.totalPages - 1 || loading}
                onClick={() => fetchLogs(currentPage + 1)}
                className="px-2.5 py-1 bg-[#202235] hover:bg-[#2b2e47] disabled:opacity-40 border border-black text-neutral-200 flex items-center gap-1 cursor-pointer"
              >
                <span>Next</span>
                <ChevronRight className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
