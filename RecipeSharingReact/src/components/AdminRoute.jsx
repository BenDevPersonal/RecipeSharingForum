import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { AuthContext } from "../context/AuthContext";
import { getMe } from "../api/users";

export function AdminRoute({ children }) {
    const { token } = useContext(AuthContext);

    const { data: user, isLoading } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
        enabled: !!token,
    });

    if (!token) {
        return <Navigate to="/" replace />;
    }

    if (isLoading) {
        return <div className="p-10 text-gray-500">Checking permissions...</div>;
    }

    if (!user || user.role !== "admin") {
        return <Navigate to="/" replace />;
    }

    return children;
}