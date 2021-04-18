import Cookies from 'js-cookie'
import { encode, decode } from "js-base64";

const TokenKey = 'X-Token'

export function getToken() {
	return Cookies.get(TokenKey)
}

export function setToken(token) {
	return Cookies.set(TokenKey, token)
}

export function removeToken() {
	return Cookies.remove(TokenKey)
}
export function getUserName() {
	var token = getToken()
	var payload = String(token).split(".")[1]
	var name = JSON.parse(decode(payload)).username
	console.log("username", name)
	return name
}

export function getUserAuth() {
	var token = getToken()
	if (token == undefined)
		return -1
	console.log(token)
	var payload = String(token).split(".")[1]
	var auth = JSON.parse(decode(payload)).authority
	console.log("authority", auth)
	return auth
}
export function getUid() {
	var token = getToken()
	var payload = String(token).split(".")[1]
	var uid = JSON.parse(decode(payload)).uid
	console.log("uid", uid)
	return uid
}