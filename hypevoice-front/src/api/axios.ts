import axios, { Axios } from "axios";

export const axiosClient: Axios = axios.create({
  baseURL: "https://hypevoice.site", // 백 URL
  headers: {
    "Content-Type": "application/json",
  },
});
