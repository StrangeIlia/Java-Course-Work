<template>
    <transition name="modal_authorization">
        <form>
            <div class="modal-mask">
                <div class="modal-wrapper">
                    <div class="modal-container">
                        <div class="modal-body">
                            <slot name="body">
                                <div>{{error}}</div>
                                <div class="form-group">
                                    <label for="reg_username">Логин</label>
                                    <input id="reg_username" type="text" class="form-control" placeholder="Введите ваш логин" v-model="username">
                                    <div class="error" v-if="invalidUserName">Логин не может быть пустым!!!</div>
                                </div>
                                <div class="form-group">
                                    <label for="reg_email">Адрес электронной почты</label>
                                    <input id="reg_email" type="email" class="form-control" placeholder="Введите ваш адрес электронной почты" v-model="email">
                                    <div class="error" v-if="invalidEmail">Email не может быть пустым!!!</div>
                                </div>
                                <div class="form-group">
                                    <label for="reg_password">Пароль</label>
                                    <input id="reg_password" type="password" class="form-control" placeholder="Введите пароль" v-model="password">
                                    <div class="error" v-if="invalidPassword">Пароль не может быть пустым!!!</div>
                                </div>
                                <div class="form-group">
                                    <label for="reg_check_password">Подтвердите пароль</label>
                                    <input id="reg_check_password" type="password" class="form-control w-100" placeholder="Подтвердите пароль" v-model="repeatPassword">
                                    <div class="error" v-if="invalidRepeatPassword">Пароли должны совпадать</div>
                                </div>
                                <div class="form-group">
                                    <button type="submit" class="btn btn-success w-100" @click="registration">Зарегистрироваться</button>
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
    import { HTTP } from "../components/http";
    const { required, maxLength, sameAs } = require('vuelidate/lib/validators');

    export default {
        name: 'Modal_Registration',

        data(){
            return {
                username: "",
                email: "",
                password: "",
                repeatPassword: "",
                error: ""
            }
        },

        computed: {
            invalidUserName : function(){
                return this.$v.username.$anyError && !this.$v.username.required;
            },

            invalidEmail : function(){
                return this.$v.email.$anyError && !this.$v.email.required;
            },

            invalidPassword : function(){
                return this.$v.password.$anyError && !this.$v.password.required;
            },

            invalidRepeatPassword : function () {
                return this.$v.repeatPassword.$anyError;
            }
        },

        validations: {
            username: {
                required,
                maxLength: maxLength(30)
            },
            email: {
                required,
                maxLength: maxLength(30)
            },
            password: {
                required,
                maxLength: maxLength(30),
            },
            repeatPassword: {
                sameAsPassword: sameAs('password')
            }
        },

        methods: {
            close : function() {
                this.error = this.repeatPassword = this.password = this.email = this.username = "";
                this.$emit('close');
            },

            registration : function () {
                this.$v.$touch();
                if(!this.$v.$invalid) {
                    HTTP.post('/users/create', {
                        username: this.username,
                        password: this.password,
                        email: this.email
                    })
                        .then(response => {
                            if(response.data.status === 'success'){
                                localStorage.token = response.data.token;
                                this.$root.username = this.username;
                                HTTP.defaults.headers['Authorization'] = 'Bearer ' + localStorage.token;
                                this.close();
                            } else {
                                this.error = response.data.error;
                            }
                        }).catch(() => this.error = 'Нет отклика от сервера');
                }
            }
        },
    };
</script>
