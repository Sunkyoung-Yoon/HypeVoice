import axios, { Axios } from "axios";

export const axiosClient: Axios = axios.create({
  baseURL: "http://localhost:8081", // 백 URL
  headers: {
    "Content-Type": "application/json",
  },
});
