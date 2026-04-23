import { api } from "./api";

export const followUser = async (userId) => {
    const res = await api.post("/api/follows", {
        followedUserId: userId
    });
    return res.data;
};

export const unfollowUser = async (userId) => {
    const res = await api.delete(`/api/follows/user/${userId}`);
    return res.data;
};

export const isFollowingUser = async (id) => {
    const res = await api.get(`/api/follows/is-following/${id}`);
    return res.data;
};

export const getFollowerCount = async (userId) => {
  const res = await api.get(`/api/follows/count/${userId}`);
  return res.data;
};