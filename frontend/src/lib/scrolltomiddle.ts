export function scrollToMiddle(node: HTMLElement) {
  function onClick() {
    const container = document.getElementById('content');
    if (!container) return;

    const containerRect = container.getBoundingClientRect();
    const elementRect = node.getBoundingClientRect();

    const offsetTop = elementRect.top - containerRect.top + container.scrollTop;
    const scrollTo = offsetTop - (container.clientHeight / 2) + (elementRect.height / 2);

    container.scrollTo({
      top: scrollTo,
      behavior: 'smooth',
    });

    window.history.replaceState(null, '', `#${node.id}`);
  }

  node.addEventListener('click', onClick);

  return {
    destroy() {
      node.removeEventListener('click', onClick);
    }
  };
}
