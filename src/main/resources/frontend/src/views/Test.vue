<template>
    <div style="display: flex; flex-direction: row; justify-content: center;">
        <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown">
                Dropdown button
            </button>
            <div class="dropdown-menu">
                <a class="dropdown-item" href="#">1-й уровень</a>
                <a class="dropdown-item submenu" href="#">1-й уровень</a>
                <div class="dropdown-menu dropdown-menu-right">
                    <a class="dropdown-item" href="#">2-й уровень</a>
                    <a class="dropdown-item submenu" href="#">2-й уровень</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="#">3-й уровень</a>
                        <a class="dropdown-item" href="#">3-й уровень</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
    export default {
        name: "Test"
    }
</script>

<style scoped>
    .dropdown-menu .dropdown-menu {
        top: auto;
        left: 100%;
        right: -100%;
    }

    .dropdown-menu .dropdown-menu-right {
        top: auto;
        left: -100%;
        right: 100%;
    }

    .dropdown-item + .dropdown-menu {
        display: block;
        opacity: 0;
        transition: opacity .3s 1s;
    }
    .dropdown-item.submenu::after {
        content: '▸';
        margin-left: 0.5rem;
    }
    .dropdown-item:hover + .dropdown-menu,
    .dropdown-menu:hover {
        opacity: 1;
        transition: opacity .3s .1s;
    }
</style>