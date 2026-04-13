import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AppContext } from "../App";

export function AdminRoute({ children }) {
  const { user } = useContext(AppContext);

  if (!user || user.role?.name !== "admin") {
    return <Navigate to="/" replace />;
  }

  return children;
}