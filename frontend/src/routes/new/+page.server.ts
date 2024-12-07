import { error } from "@sveltejs/kit";

export async function load() {
    if (process.env.DISABLE_NEW) {
        error(404, {
            message: "Not Found",
        });
    }
}