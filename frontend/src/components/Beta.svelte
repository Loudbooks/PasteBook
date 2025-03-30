<a class="beta" target="_blank" href="https://github.com/Loudbooks/PastaBook"
  >BETA</a
>

<style lang="scss">
  .beta {
    position: fixed;
    margin: 0;
    right: 100px;
    bottom: 0;
    background: linear-gradient(90deg, #f05a48, #f4518d);
    color: #b21807;;
    padding: 3px 8px;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
    font-size: 12px;
    font-weight: bold;
    z-index: 9999;
    font-family: Comic Neue, sans-serif;
    text-decoration: none;

    animation: flyIn ease 0.7s;
    animation-iteration-count: 1;

    transition: transform 0.3s ease;
  }

  .beta:hover {
    cursor: pointer;
    transform: translateY(10%);
  }
</style>
