'use strict';
// CSR (renderizado en el cliente): las tablas se pintan con respuestas REST.
const $ = (selector) => document.querySelector(selector);
const csrfHeader = $('meta[name="_csrf_header"]').content;
const csrfToken = $('meta[name="_csrf"]').content;

async function api(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    credentials: 'same-origin',
    headers: {
      Accept: 'application/json',
      ...(options.body ? {'Content-Type': 'application/json'} : {}),
      ...(options.method && options.method !== 'GET' ? {[csrfHeader]: csrfToken} : {}),
      ...options.headers
    }
  });
  if (response.status === 401) { window.location.href = '/login'; throw new Error('Sesión expirada'); }
  if (!response.ok) {
    let error;
    try { error = (await response.json()).mensaje; } catch (_) { error = null; }
    throw new Error(error || `Error HTTP ${response.status}`);
  }
  return response.status === 204 ? null : response.json();
}
function showError(error) {
  const el = $('#globalError');
  el.textContent = error.message || String(error);
  el.hidden = false;
}
function formatDate(value) {
  if (!value) return '—';
  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? value : date.toLocaleString('es-MX', {dateStyle:'short', timeStyle:'short'});
}
function td(text) { const el = document.createElement('td'); el.textContent = String(text); return el; }
function emptyRow(message, columns) {
  const tr = document.createElement('tr'); const cell = td(message);
  cell.colSpan = columns; cell.className = 'empty'; tr.appendChild(cell); return tr;
}
function paintResult(item) {
  $('#resultBadge').hidden = false;
  $('#resultHint').hidden = true;
  $('#resultValue').textContent = Number(item.valorImc).toFixed(2);
  $('#resultState').textContent = item.estado;
}
async function loadHistory() {
  const body = $('#historyBody');
  const {data: list} = await api('/api/imc');
  body.replaceChildren();
  if (!list.length) { body.appendChild(emptyRow('Aún no tienes mediciones registradas.', 5)); return; }
  list.forEach(item => {
    const tr = document.createElement('tr');
    tr.append(td(formatDate(item.fecha)), td(Number(item.peso).toFixed(2) + ' kg'),
              td(Number(item.estatura).toFixed(2) + ' m'), td(Number(item.valorImc).toFixed(2)));
    const state = td(''); const badge = document.createElement('span');
    badge.textContent = item.estado;
    badge.className = 'status-tag' + (item.estado === 'Sobrepeso' ? ' warning' : (item.estado !== 'Saludable' ? ' danger' : ''));
    state.appendChild(badge); tr.appendChild(state); body.appendChild(tr);
  });
  paintResult(list[0]);
}
async function loadFolders() {
  const {data: folders} = await api('/api/directory');
  const select = $('#folderSelect'); select.replaceChildren();
  folders.forEach(folder => {
    const opt = document.createElement('option'); opt.value = folder.id; opt.textContent = folder.nombre;
    select.appendChild(opt);
  });
}
async function loadFiles() {
  const {data: files} = await api('/api/file');
  const list = $('#filesList'); list.replaceChildren();
  if (!files.length) { const li=document.createElement('li');li.textContent='Sin reportes todavía.';list.appendChild(li);return; }
  files.forEach(file => {
    const li=document.createElement('li'), title=document.createElement('span'), link=document.createElement('a');
    title.textContent=file.nombre; link.textContent='Descargar CSV';
    link.href=`/api/file/${encodeURIComponent(file.id)}/download`;
    li.append(title,link);list.appendChild(li);
  });
}
async function loadNotices() {
  const {data: notes} = await api('/api/notify');
  const list = $('#notificationsList'); list.replaceChildren();
  if (!notes.length) {const li=document.createElement('li');li.textContent='No hay notificaciones.';list.appendChild(li);return;}
  notes.slice(0,5).forEach(note => {
    const li=document.createElement('li'), wrapper=document.createElement('div'), msg=document.createElement('span'), time=document.createElement('small');
    msg.textContent=note.mensaje;time.textContent=formatDate(note.fecha);wrapper.append(msg,time);li.appendChild(wrapper);
    list.appendChild(li);
  });
}
$('#imcForm').addEventListener('submit', async event => {
  event.preventDefault();$('#globalError').hidden = true;
  const button=$('#imcForm button');button.disabled=true;
  try {
    const peso=Number($('#peso').value), estatura=Number($('#altura').value);
    if (!(peso>0) || estatura<1 || estatura>2.5) throw new Error('Ingresa peso positivo y estatura entre 1 y 2.5 m');
    const result=await api('/api/imc',{method:'POST',body:JSON.stringify({peso,estatura})});
    paintResult(result.data);$('#peso').value='';
    await Promise.all([loadHistory(),loadNotices()]);
  } catch (e) {showError(e);} finally {button.disabled=false;}
});
$('#createFolder').addEventListener('click', async () => {
  const name=$('#folderName').value.trim();if(!name)return showError(new Error('Escribe el nombre de la carpeta.'));
  try {
    await api('/api/directory',{method:'POST',body:JSON.stringify({nombre:name})});
    $('#folderName').value='';await loadFolders();$('#filesFeedback').textContent='Carpeta creada.';
  } catch(e){showError(e);}
});
$('#createReport').addEventListener('click', async () => {
  const nombre=$('#reportName').value.trim(),directorioId=Number($('#folderSelect').value);
  if(!nombre||!directorioId)return showError(new Error('Selecciona una carpeta y un nombre.'));
  try {
    await api('/api/file',{method:'POST',body:JSON.stringify({nombre,directorioId})});
    await loadFiles();$('#filesFeedback').textContent='Reporte generado. Puedes descargarlo abajo.';
  } catch(e){showError(e);}
});
Promise.all([loadHistory(),loadFolders(),loadFiles(),loadNotices()]).catch(showError);
