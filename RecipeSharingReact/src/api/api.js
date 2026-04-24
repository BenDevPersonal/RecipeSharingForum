import Axios from "axios";

export const api = Axios.create({
  baseURL: "http://localhost:8080",
});

export const toggleBookmark = (postId) =>
  api.post(`/api/bookmarks/${postId}`);

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/";
    }

    return Promise.reject({
      message:
        err.response?.data?.message ||
        "Something went wrong",
    });
  }
);