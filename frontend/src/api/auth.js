import request from './request'

/**
 * 用户登录
 */
export function login(data) {
  return request({
    url: '/login/userlogin',
    method: 'post',
    data
  })
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

/**
 * 退出登录
 */
export function logout() {
  return request({
    url: '/login/logout',
    method: 'post'
  })
}
