import { atom } from 'recoil';

export const curStorageSizeAtom = atom({
  key: 'curStorageSize', // unique ID (with respect to other atoms/selectors)
  default: 0, // default value (aka initial value)
});