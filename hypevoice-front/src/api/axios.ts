import axios, { Axios } from "axios";ZZ
import { SERVER_URL } from "@/recoil/SERVER_URL";
import { useRecoilValue } from "recoil";

export const axiosClient: Axios = axios.create({
  baseURL: useRecoilValue(SERVER_URL),
  headers: {
    "Content-Type": "application/json",
  },
});