"use client";

import { useCallback, useEffect, useLayoutEffect, useState } from "react";
import { useEventFilters } from "@/hooks/useEventFilters";
import { useReducedMotion } from "@/hooks/useReducedMotion";
import { EVENTS, BIOMES, type EventCategory, type FestEvent } from "@/data/events";
import { api } from "@/lib/api";
import { PixelHeading } from "../branding/PixelHeading";
import { EmptyEventsState } from "./EmptyEventsState";
import { EventDetailsModal } from "./EventDetailsModal";
import { EventFilters } from "./EventFilters";
import { EventGrid } from "./EventGrid";
import { EventsHero } from "./EventsHero";

/** Client island for the events page: owns filter state and the selected event. */
export function EventsExperience() {
  const [eventList, setEventList] = useState<FestEvent[]>(EVENTS as FestEvent[]);

  // Connect to Spring Boot backend to retrieve live events from PostgreSQL
  useEffect(() => {
    let mounted = true;
    api
      .getPublicEvents()
      .then((backendEvents) => {
        if (!mounted || !backendEvents || backendEvents.length === 0) return;
        const validCategories = Object.keys(BIOMES) as EventCategory[];
        const mapped: FestEvent[] = backendEvents.map((be) => {
          // Match category or default to Coding
          const matchedCategory =
            validCategories.find(
              (c) =>
                c.toLowerCase() === be.categoryName.toLowerCase() ||
                c.toLowerCase() === be.categoryCode.toLowerCase(),
            ) || "Coding";

          return {
            id: be.slug,
            title: be.title,
            summary: be.summary,
            description: be.summary,
            category: matchedCategory,
            venue: be.venue,
            prize: be.prize,
            teamSize: be.teamSize,
            duration: be.duration,
            registrationUrl: be.registrationUrl,
            participation: (be.participation as any) || "Individual",
            color: be.categoryColor,
          };
        });
        setEventList(mapped);
      })
      .catch(() => {
        // If backend is not currently running, keep fallback sample events
      });
    return () => {
      mounted = false;
    };
  }, []);

  const filters = useEventFilters(eventList);
  const [selected, setSelected] = useState<FestEvent | null>(null);
  const [detailsOpen, setDetailsOpen] = useState(false);
  const reduced = useReducedMotion();
  const { setCategory, setQuery } = filters;

  // Close the details panel if the page is hidden by navigation.
  useLayoutEffect(() => () => setDetailsOpen(false), []);

  const openDetails = useCallback((event: FestEvent) => {
    setSelected(event);
    setDetailsOpen(true);
  }, []);

  const pickCategory = useCallback(
    (category: EventCategory) => {
      setQuery("");
      setCategory(category);
      document.getElementById("events")?.scrollIntoView({ behavior: reduced ? "auto" : "smooth" });
    },
    [reduced, setCategory, setQuery],
  );

  return (
    <>
      <EventsHero eventCount={eventList.length} onPickCategory={pickCategory} />

      <section
        id="events"
        aria-labelledby="events-list-title"
        className="relative scroll-mt-20 border-t-2 border-black bg-[rgb(8_9_14/0.78)] shadow-[inset_0_2px_0_rgb(255_255_255/0.06)] backdrop-blur-sm"
      >
        <div className="mx-auto grid max-w-7xl gap-8 px-4 py-14 sm:px-6">
          <PixelHeading id="events-list-title" eyebrow="Quest board">
            All events
          </PixelHeading>
          <EventFilters
            query={filters.query}
            onQueryChange={filters.setQuery}
            category={filters.category}
            onCategoryChange={filters.setCategory}
            counts={filters.counts}
            resultCount={filters.results.length}
            total={eventList.length}
          />
          {filters.results.length > 0 ? (
            <EventGrid events={filters.results} onSelect={openDetails} />
          ) : (
            <EmptyEventsState onReset={filters.reset} />
          )}
        </div>
      </section>

      <EventDetailsModal event={selected} open={detailsOpen} onClose={() => setDetailsOpen(false)} />
    </>
  );
}
