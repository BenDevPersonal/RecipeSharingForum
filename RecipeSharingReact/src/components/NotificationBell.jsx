import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useEffect, useRef, useState } from "react";
import {
  getMyNotifications,
  getUnreadCount,
  markAllAsRead
} from "../api/notifications";

export function NotificationBell() {
  const [open, setOpen] = useState(false);
  const queryClient = useQueryClient();
  const wrapperRef = useRef(null);

  const { data: notifications = [] } = useQuery({
    queryKey: ["notifications"],
    queryFn: getMyNotifications,
  });

  const { data: unread = 0 } = useQuery({
    queryKey: ["notifications-unread"],
    queryFn: getUnreadCount,
  });

  const markAllMutation = useMutation({
    mutationFn: markAllAsRead,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["notifications"] });
      queryClient.invalidateQueries({ queryKey: ["notifications-unread"] });
    }
  });

  // ✅ close on ESC + outside click
  useEffect(() => {
    function handleKeyDown(e) {
      if (e.key === "Escape") {
        setOpen(false);
      }
    }

    function handleClickOutside(e) {
      if (wrapperRef.current && !wrapperRef.current.contains(e.target)) {
        setOpen(false);
      }
    }

    document.addEventListener("keydown", handleKeyDown);
    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <div className="relative" ref={wrapperRef}>
      {/* Bell */}
      <button
        onClick={() => setOpen(prev => !prev)}
        className="relative text-xl hover:text-accent"
      >
        🔔

        {unread > 0 && (
          <span className="relative -top-3 -left-1 bg-red-500 text-white text-[10px] px-1 rounded-full">
            {unread}
          </span>
        )}
      </button>

      {/* Dropdown */}
      {open && (
        <div className="absolute right-0 mt-2 w-80 bg-white dark:bg-gray-900 border border-gray-100 dark:border-gray-700 rounded-xl shadow-lg z-50">
          <div className="p-2 flex justify-between items-center border-b border-gray-100 dark:border-gray-700">
            <span className="text-sm font-semibold">Notifications</span>

            <button
              onClick={() => markAllMutation.mutate()}
              className="text-xs text-accent hover:underline"
            >
              Mark all read
            </button>
          </div>

          <div className="max-h-80 overflow-y-auto">
            {notifications.length === 0 ? (
              <p className="p-3 text-sm text-gray-500">No notifications</p>
            ) : (
              notifications.map(n => (
                <div
                  key={n.id}
                  className={`p-3 text-sm border-b border-gray-100 dark:border-gray-700 hover:bg-gray-100 dark:hover:bg-gray-800 ${
                    !n.read ? "font-semibold" : "opacity-70"
                  }`}
                >
                  {n.message}
                </div>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
}