import Axios from "axios";

export const api = Axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const customError = {
      message:
        error.response?.data?.message ||
        "Something went wrong. Please try again.",
      status: error.response?.status,
    };

    return Promise.reject(customError);
  }
);