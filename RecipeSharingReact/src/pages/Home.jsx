import { useContext } from "react"
import { AppContext } from "../App"
import { useQuery } from "@tanstack/react-query";
import Axios from 'axios';

export function Home() {
    const { user } = useContext(AppContext);

    return (
        <div>
            <h1>Home page, the user name is {user}</h1>
        </div>
    )
}