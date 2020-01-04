<template>
    <transition name="modal_authorization">
        <form>
            <div class="modal-mask">
                <div class="modal-wrapper">
                    <div class="modal-container">
                        <div class="modal-body">
                            <slot name="body">
                                <div>{{errors.method}}</div>
                                <div class="form-group">
                                    <label for="auth_username">Логин</label>
                                    <input id="auth_username" type="text" class="form-control" placeholder="Введите ваш логин" v-model="username">
                                    <div class="error" v-if="invalidUserName">Логин не может быть пустым!!!</div>
                                </div>
                                <div class="form-group">
                                    <label for="auth_password">Пароль</label>
                                    <input id="auth_password" type="password" class="form-control" placeholder="Введите пароль" v-model="password">
                                    <div class="error" v-if="invalidPassword">Пароль не может быть пустым!!!</div>
                                </div>
                                <div class="form-group">
                                    <label><input type="checkbox" name="checkbox" v-model="rememberMe">Запомнить меня</label>
                                </div>
                                <div class="form-group">
                                    <button type="submit" class="btn btn-success w-100" @click="login">Войти</button>
                                </div>
                                <div class="form-group">
                                    <button class="btn btn-primary w-100" @click="close">Вернуться на главную страницу</button>
                                </div>
                            </slot>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </transition>
</template>

<script>
    import {HTTP} from "./http";
    const { required, maxLength } = require('vuelidate/lib/validators');

    export default {
        name: 'Modal_Authorization',

        data(){
            return {
                username: "",
                password: "",
                rememberMe: false,
                errors: {
                    method: ""
                }
            }
        },

        computed: {
            invalidUserName : function(){
                return this.$v.username.$anyError && !this.$v.username.required;
            },

            invalidPassword : function(){
                return this.$v.password.$anyError && !this.$v.password.required;
            },
        },

        validations: {
            username: {
                required,
                maxLength: maxLength(30)
            },
            password: {
                required,
                maxLength: maxLength(30)
            }
        },

        methods: {
            close : function() {
                this.username = this.password = this.errors.method = "";
                this.rememberMe = false;
                this.$emit('close');
            },

            login : function () {
                this.$v.$touch();
                if(!this.$v.$invalid) {
                    HTTP.post('/site/login', {
                        username: this.username,
                        password: this.password,
                        rememberMe: this.rememberMe
                    })
                        .then(response => {
                            if(response.data.status === 'success'){
                                localStorage.token = response.data.token;
                                this.$root.username = this.username;
                                HTTP.defaults.headers['Authorization'] = 'Bearer ' + localStorage.token;
                                this.close();
                            } else {
                                this.$root.username = '';
                                delete localStorage.token;
                                this.errors.method = response.data;
                            }
                        }).catch(() => this.errors.method = 'Нет отклика от сервера');
                }
            }
        },
    };
</script>
