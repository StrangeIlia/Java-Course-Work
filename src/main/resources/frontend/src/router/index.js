import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import ContextPage from "../views/ContextPage";
import FavoriteVideos from "../views/FavoriteVideos";
import LoadedVideos from "../views/LoadedVideos";
import Playlists from "../views/Playlists";
import UserPage from "../views/UserPage";
import Test from "../views/Test";
import Page_DeleteVideo from "../views/Page_DeleteVideo";
import Page_UpdateVideo from "../views/Page_UpdateVideo";

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/test',
    name: 'test',
    component: Test
  },
  {
    path: '/video/:id',
    name: 'video',
    props: true,
    component: ContextPage,
  },
  {
    path: "/favorite_videos",
    name: 'favorite_videos',
    component: FavoriteVideos
  },
  {
    path: "/loaded_videos",
    name: 'loaded_videos',
    component: LoadedVideos
  },
  {
    path: "/user_playlists",
    name: 'user_playlists',
    component: Playlists
  },
  {
    path: '/user_page/:username',
    name: 'user_page',
    component: UserPage
  },
  {
    path: '/update_video',
    name: 'update_video',
    component: Page_UpdateVideo
  },
  {
    path: '/delete_video',
    name: 'delete_video',
    component: Page_DeleteVideo
  }
];

const router = new VueRouter({
  routes
});

export default router
