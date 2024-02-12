import { Cookies } from "react-cookie";

const cookies = new Cookies(); // 쿠키

// 쿠키 이름, 값, 옵션 순서
export function setCookie(name: string, value: string, options?: any) {
  return cookies.set(name, value, { ...options });
}

export function getCookie(name: string) {
  return cookies.get(name);
}

export function removeCookie(name: string) {
  return cookies.remove(name);
}
