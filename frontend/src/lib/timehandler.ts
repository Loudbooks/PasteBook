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
  const currentTime = new Date();
  const timeDifference = targetTime - currentTime;
  const seconds = Math.floor(timeDifference / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  let formattedTime = "";
  if (days > 0) {
    if (days === 1) {
      formattedTime += days + " day ";
    } else {
      formattedTime += days + " days ";
    }
  }
  if (hours % 24 > 0) {
    if (hours % 24 === 1) {
      formattedTime += (hours % 24) + " hour ";
    } else {
      formattedTime += (hours % 24) + " hours ";
    }
  }
  if (minutes % 60 > 0) {
    if (minutes % 60 === 1) {
      formattedTime += (minutes % 60) + " minute ";
    } else {
      formattedTime += (minutes % 60) + " minutes ";
    }
  }
  if (seconds % 60 > 0) {
    if (seconds % 60 === 1) {
      formattedTime += (seconds % 60) + " second ";
    } else {
      formattedTime += (seconds % 60) + " seconds";
    }
  }

  return formattedTime;
}