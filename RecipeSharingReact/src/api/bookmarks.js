import { api } from "./api";

export const toggleBookmark = async (postId) => {
  await api.post(`/api/bookmarks/${postId}`);
};

export const getBookmarkedPosts = async () => {
  const res = await api.get("/api/bookmarks/me");
  return res.data;
};