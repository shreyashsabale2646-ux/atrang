"use client";

import React, { useState } from "react";
import Link from "next/link";
import { ArrowLeft, Lock, User, AlertCircle, Loader2 } from "lucide-react";
import { useAuth } from "@/context/AuthContext";

export default function AdminLoginPage() {
  const { login } = useAuth();
  const [username, setUsername] = useState("admin");
  const [password, setPassword] = useState("admin123");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);

    try {
      await login(username, password);
    } catch (err: any) {
      setError(err?.message || "Invalid credentials. Please verify username and password.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-4 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-[#1c1d2d] via-[#0d0e15] to-[#07080b]">
      {/* Return link */}
      <Link
        href="/"
        className="mb-6 inline-flex items-center gap-2 text-sm font-mono text-neutral-400 hover:text-gold transition"
      >
        <ArrowLeft className="w-4 h-4" />
        Back to Atrang 2026 Home
      </Link>

      {/* Login Box */}
      <div className="w-full max-w-md bg-[#161724] border-4 border-black shadow-[8px_8px_0px_#000] p-6 sm:p-8 relative">
        <div className="absolute -top-3.5 left-1/2 -translate-x-1/2 bg-[#a46bff] border-2 border-black px-4 py-0.5 shadow-[2px_2px_0px_#000]">
          <span className="font-pixel text-xs text-white uppercase tracking-widest">
            DBMS MINI-PROJECT
          </span>
        </div>

        <div className="text-center mb-6 mt-2">
          <div className="inline-flex w-12 h-12 bg-black border-2 border-gold shadow-[3px_3px_0px_#000] items-center justify-center mb-3">
            <Lock className="w-6 h-6 text-gold" />
          </div>
          <h1 className="font-pixel text-2xl text-gold tracking-wider uppercase">
            Admin Authentication
          </h1>
          <p className="text-xs text-neutral-400 font-mono mt-1">
            IIIT Tiruchirappalli — Festival Administration
          </p>
        </div>

        {error && (
          <div className="mb-5 p-3 bg-[#450a0a] border-2 border-[#b91c1c] text-red-200 text-xs font-mono flex items-start gap-2 shadow-[2px_2px_0px_#000]">
            <AlertCircle className="w-4 h-4 shrink-0 text-red-400 mt-0.5" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block font-pixel text-xs uppercase tracking-wider text-neutral-300 mb-1">
              Username
            </label>
            <div className="relative">
              <User className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                required
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="admin"
                className="w-full bg-[#0d0e15] border-2 border-black focus:border-[#a46bff] text-white pl-10 pr-3 py-2 text-sm font-mono shadow-[inset_2px_2px_4px_rgba(0,0,0,0.6)] outline-none"
              />
            </div>
          </div>

          <div>
            <label className="block font-pixel text-xs uppercase tracking-wider text-neutral-300 mb-1">
              Password
            </label>
            <div className="relative">
              <Lock className="w-4 h-4 text-neutral-400 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-[#0d0e15] border-2 border-black focus:border-[#a46bff] text-white pl-10 pr-3 py-2 text-sm font-mono shadow-[inset_2px_2px_4px_rgba(0,0,0,0.6)] outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="w-full mt-2 py-3 bg-[#15803d] hover:bg-[#16a34a] disabled:opacity-50 text-white font-pixel text-sm tracking-wider uppercase border-2 border-black shadow-[4px_4px_0px_#000] active:translate-y-0.5 active:shadow-[2px_2px_0px_#000] transition flex items-center justify-center gap-2 cursor-pointer"
          >
            {submitting ? (
              <>
                <Loader2 className="w-4 h-4 animate-spin" />
                <span>Authenticating...</span>
              </>
            ) : (
              <span>ENTER COMMAND CENTER</span>
            )}
          </button>
        </form>

        <div className="mt-6 pt-4 border-t-2 border-black/40 text-center text-[11px] font-mono text-neutral-400">
          <p className="text-neutral-300 font-semibold mb-1">Evaluation Viva Default:</p>
          <p className="text-neutral-400">
            Username: <span className="text-gold font-bold">admin</span> &bull; Password:{" "}
            <span className="text-gold font-bold">admin123</span>
          </p>
        </div>
      </div>
    </div>
  );
}
