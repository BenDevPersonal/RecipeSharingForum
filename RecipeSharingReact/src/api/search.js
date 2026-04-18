import { api } from "./api";

export const searchPosts = async (q, category, allergy) => {
  const res = await api.get("/api/posts/search", {
    params: { q, category, allergy },
  });
  return res.data;
};

export const searchUsers = async (q) => {
  const res = await api.get("/api/users/search", {
    params: { q },
  });
  return res.data;
};