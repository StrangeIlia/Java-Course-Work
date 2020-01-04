<template>
  <div class="album py-5">
    <div class="container">
      <div class="row">
        <div v-for="video in videos" :key="video.id" class="col-md-3">
          <router-link :to="{name:'video', params:{id:video.id} }">
            <div class="card mb-4 shadow-sm">
              <img :src="video.preview" alt = "Невозможно" width="100%" height="100%">
              <div class = "card-body">
                <div class="card-text">
                  {{video.name}}
                </div>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { HTTP } from "../components/http";
  export default {
    name: 'Videos',
    data () {
      return{
        videos: [],
      }
    },

    created() {
      HTTP.get('/videos')
              .then(response => {this.videos = response.data;});
    }
  }

</script>