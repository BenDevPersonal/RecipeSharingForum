import { api } from "./api";

export const getMyNotifications = async () => {
    const res = await api.get("/api/notifications/me")
    return res.data;
};

export const getUnreadCount = async () => {
    const res = await api.get("/api/notifications/unread/count");
    return res.data;
}

export const markAllAsRead = async () => {
    const res = await api.patch("/api/notifications/mark-all-read");
    return res.data;
}