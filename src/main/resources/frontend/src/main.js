import Vue from 'vue'
import App from './App.vue'
import router from './router'
import {HTTP} from "./components/http";
import Vuelidate from "vuelidate/src";

Vue.config.productionTip = false;

Vue.use(Vuelidate);

export let MainVue = new Vue({
  name: 'Main',
  router,
  data() {
    return {
      username : ''
    }
  },

  render: h => h(App),

  created() {
    if(localStorage.token !== undefined){
      HTTP.get('/site/get_username').then(response => {
        if(response.data.username !== null)
          this.username = response.data.username;
      }).catch(() => delete localStorage.token);
    }
  },

  computed: {
    successAuth : function () {
      return this.username !== '';
    }
  }

});

MainVue.$mount('#app');
