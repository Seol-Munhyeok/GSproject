import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
// PrimeVue 추가
import PrimeVue from 'primevue/config';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';

// PrimeVue 스타일
import 'primevue/resources/themes/lara-light-green/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import InputText from 'primevue/inputtext';

const app = createApp(App);

app.use(store);
app.use(router);
app.use(PrimeVue);
app.component('DataTable', DataTable);
app.component('Column', Column);
app.component('InputText', InputText);
app.mount('#app');
