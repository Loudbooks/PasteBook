import { writable } from "svelte/store";

export const wrap = writable(true);
export const time = writable("24h");
export const burn = writable(false);