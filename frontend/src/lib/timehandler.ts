export function formatTimeSince(created: number): string {
  let timeSinceStr: string;

  let timeSince = Date.now() - created;

  if (timeSince < 0) {
    timeSinceStr = "";
    return timeSinceStr;
  }

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

export function formatTimeUntil(targetTime: number) {
  const currentTime = new Date().getTime();
  let timeDifference = targetTime - currentTime;

  if (timeDifference <= 0) return "0 seconds";

  const seconds = Math.ceil(timeDifference / 1000);
  const minutes = Math.ceil(seconds / 60);
  const hours = Math.ceil(minutes / 60);
  const days = Math.ceil(hours / 24);

  if (days >= 1) {
    return days === 1 ? "1 day" : `${days} days`;
  } else if (hours >= 1) {
    return hours === 1 ? "1 hour" : `${hours} hours`;
  } else if (minutes >= 1) {
    return minutes === 1 ? "1 minute" : `${minutes} minutes`;
  } else {
    return seconds === 1 ? "1 second" : `${seconds} seconds`;
  }
}

