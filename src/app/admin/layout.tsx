"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import React, { useEffect } from "react";
import {
  LayoutDashboard,
  Calendar,
  Layers,
  FileText,
  LogOut,
  ExternalLink,
  ShieldCheck,
  Loader2,
} from "lucide-react";
import { useAuth } from "@/context/AuthContext";
import { cn } from "@/lib/utils";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const { user, loading, logout } = useAuth();
  const pathname = usePathname();
  const router = useRouter();

  const isLoginPage = pathname === "/admin/login";

  useEffect(() => {
    if (!loading && !user && !isLoginPage) {
      router.push("/admin/login");
    }
  }, [user, loading, isLoginPage, router]);

  if (isLoginPage) {
    return <div className="min-h-screen bg-[#0e0e14] text-white">{children}</div>;
  }

  if (loading) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-[#0e0e14] text-white">
        <Loader2 className="w-10 h-10 animate-spin text-[#a46bff] mb-4" />
        <p className="font-pixel text-lg tracking-wider text-neutral-300">
          Loading Atrang Command Center...
        </p>
      </div>
    );
  }

  if (!user) {
    return null;
  }

  const navLinks = [
    { label: "Dashboard", href: "/admin", icon: LayoutDashboard },
    { label: "Events", href: "/admin/events", icon: Calendar },
    { label: "Categories", href: "/admin/categories", icon: Layers },
    { label: "Audit Logs", href: "/admin/audit-logs", icon: FileText },
  ];

  return (
    <div className="min-h-screen flex flex-col md:flex-row bg-[#0b0c10] text-neutral-100">
      {/* Sidebar */}
      <aside className="w-full md:w-64 bg-[#14151f] border-b-2 md:border-b-0 md:border-r-2 border-black flex flex-col shrink-0 shadow-2xl">
        {/* Brand Header */}
        <div className="p-4 border-b-2 border-black bg-[#1b1c2b] flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 bg-[#a46bff] border-2 border-black shadow-[2px_2px_0px_#000] flex items-center justify-center font-pixel text-white text-xl">
              A
            </div>
            <div>
              <h2 className="font-pixel text-base text-gold tracking-wide">ATRANG ADMIN</h2>
              <p className="text-[10px] text-neutral-400 font-mono">DBMS Portal 2026–27</p>
            </div>
          </div>
          <span className="flex h-2.5 w-2.5 rounded-full bg-emerald-400 shadow-[0_0_8px_#34d399]" />
        </div>

        {/* User Card */}
        <div className="p-3 border-b-2 border-black bg-[#161724]">
          <div className="flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-[#a46bff]" />
            <span className="font-pixel text-xs text-neutral-200 truncate">{user.fullName}</span>
          </div>
          <div className="text-[11px] text-neutral-400 font-mono mt-0.5">
            Role: <span className="text-[#38bdf8]">{user.role}</span>
          </div>
        </div>

        {/* Navigation */}
        <nav className="p-3 flex-1 space-y-1.5 overflow-y-auto">
          {navLinks.map((item) => {
            const active = pathname === item.href;
            const Icon = item.icon;
            return (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  "flex items-center gap-3 px-3 py-2.5 font-pixel text-sm tracking-wider uppercase transition-all border-2",
                  active
                    ? "bg-[#25283d] text-gold border-black shadow-[2px_2px_0px_#000]"
                    : "text-neutral-300 border-transparent hover:border-black hover:bg-[#1a1b28] hover:text-white"
                )}
              >
                <Icon className={cn("w-4 h-4", active ? "text-gold" : "text-neutral-400")} />
                <span>{item.label}</span>
              </Link>
            );
          })}
        </nav>

        {/* Quick Links & Logout */}
        <div className="p-3 border-t-2 border-black bg-[#10111a] space-y-2">
          <Link
            href="/events"
            target="_blank"
            className="flex items-center justify-between px-3 py-2 text-xs font-mono text-neutral-400 hover:text-white bg-[#191a27] border border-black hover:border-neutral-600 transition"
          >
            <span>Public Website</span>
            <ExternalLink className="w-3.5 h-3.5" />
          </Link>
          <button
            onClick={() => logout()}
            className="w-full flex items-center justify-center gap-2 px-3 py-2 bg-[#7f1d1d] hover:bg-[#991b1b] text-white font-pixel text-xs tracking-wider border-2 border-black shadow-[2px_2px_0px_#000] active:translate-y-0.5 transition"
          >
            <LogOut className="w-3.5 h-3.5" />
            <span>LOGOUT</span>
          </button>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className="flex-1 min-w-0 p-4 sm:p-6 lg:p-8 overflow-y-auto max-h-screen">
        {children}
      </main>
    </div>
  );
}
