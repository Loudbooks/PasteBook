export function formatTimeSince(created: number): string {
    let timeSinceStr: string;

    let timeSince = Date.now() - created;
    if (timeSince < 1000) {
        timeSinceStr = "Just now";
    } else if (timeSince < 60000) {
        if (Math.floor(timeSince / 1000) == 1) {
            timeSinceStr = "1 second ago";
        } else {
            timeSinceStr = Math.floor(timeSince / 1000) + " seconds ago";
        }
    } else if (timeSince < 3600000) {
        if (Math.floor(timeSince / 60000) == 1) {
            timeSinceStr = "1 minute ago";
        } else {
            timeSinceStr = Math.floor(timeSince / 60000) + " minutes ago";
        }
    } else if (timeSince < 86400000) {
        if (Math.floor(timeSince / 3600000) == 1) {
            timeSinceStr = "1 hour ago";
        } else {
            timeSinceStr = Math.floor(timeSince / 3600000) + " hours ago";
        }
    } else {
        if (Math.floor(timeSince / 86400000) == 1) {
            timeSinceStr = "1 day ago";
        } else {
            timeSinceStr = Math.floor(timeSince / 86400000) + " days ago";
        }
    }

    return timeSinceStr;
}