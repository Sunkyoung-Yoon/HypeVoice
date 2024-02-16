import { atom } from 'recoil';

export const SERVER_URL = atom({
  key: 'SERVER_URL', // unique ID (with respect to other atoms/selectors)
  default: "https://hypevoice.site", // default value (aka initial value)
});