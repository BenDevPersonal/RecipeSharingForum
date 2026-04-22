import { api } from "./api";

export const blacklistUser = async (userId) => {
    const res = await api.post("/api/blacklist", {
        blacklistedUserId: userId
    });
    return res.data;
};

export const unblacklistUser = async (userId) => {
    const res = await api.delete(`/api/blacklist/user/${userId}`);
    return res.data;
};

export const isBlacklistedUser = async (id) => {
    const res = await api.get(`/api/blacklist/is-blacklisted/${id}`);
    return res.data;
};

export const getBlacklists = async () => {
    const res = await api.get(`/api/blacklist/`)
    return res.data;
}