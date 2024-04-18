<script>
    import {MetaTags} from "svelte-meta-tags";
    import extend from 'just-extend';

    export let data;

    $: metaTags = extend(true, {}, data.baseMetaTags, $page.data.pageMetaTags);
</script>

<MetaTags {...metaTags}/>

<slot />