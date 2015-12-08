package co.com.seguridog

import groovy.sql.Sql
import javax.imageio.ImageIO
import java.awt.image.BufferedImage;

class K9AdminController {

    def dataSource
    BufferedImage image

    def index() {}

    def calendar() {
        def activities_list = WorkCanine.findAll()
        def exercise_ability_list = ExerciseAbility.findAll()
        def clinic_history_list = ClinicHistory.findAll()
        [activities_list : activities_list, exercise_ability_list : exercise_ability_list, clinic_history_list : clinic_history_list]
    }

    def view_profile_user(){
        def user = K9Admin.findById(session.user.id)
        [user : user]
    }

    def edit_profile_user(){
        def edit_user = K9Admin.findById(session.user.id)
        [edit_user : edit_user]
    }

    def save_profile_user(){
        Sql sql = Sql.newInstance(dataSource)
        sql.executeUpdate('update k9user set first_name = ?, last_name = ?, login_user = ?, login_pass = ?, birth_date = ?, cellphone = ?, e_mail = ? where id = ?',
                [params.firstName,params.lastName,params.loginUser,params.loginPass,params.birthDate,params.cellphone,params.eMail,session.user.id])
        redirect(controller: "K9Admin", action: "view_profile_user")
    }

    def register_user(){}

    def user_register = {
        redirect(controller: "K9Admin", action: "register_user")
    }

    def users_list = {
        def list = K9User.findAllByEnableUser(true)
        [users:list]
    }

    def dogs_list = {
        def lista = Canine.findAll()
        [dogs:lista]
    }

    def list_services = {
        def service = WorkCanine.findAll()
        [servi:service]
    }

    def disable_user = {
        Sql sql = Sql.newInstance(dataSource)
        sql.executeUpdate('update k9user set enable_user = ? where id = ?',[false,params.id_user])
        redirect(controller: "K9Admin", action: "index")
    }

    def register_dog(){}

    def dog_register = {
        redirect(controller: "K9Admin", action: "register_dog")
    }

    def save_data_dog = {
        image = ImageIO.read( new File( params.path+"\\"+ params.photoCanine ) )
        ImageIO.write(image, params.photoCanine.substring(params.photoCanine.length()-3,params.photoCanine.length()) , new File( "${new File("").getAbsolutePath()}\\web-app\\purpose\\img\\canines\\"+params.photoCanine ) )
        Sql sql = Sql.newInstance(dataSource)
        sql.execute('insert into canine(version,photo_canine,color_canine,date_birthday,micro_chip,name_canine,sex_canine,state_canine,type_race,attend_call,felt_down,name_father,name_mother,position_stay,sign_canine,sit_down,walk_side,watch_canine) values (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)',
                [4,params.photoCanine,params.color_canine,params.date_birthday,params.micro_chip,params.name_canine,params.sex_canine,'Activo',params.type_race,'','',params.name_father,params.name_mother,'','','','',''])
        redirect(controller: "K9Admin", action: "index")
    }

    def save_data_user = {
        def newUser
        if( params.typeUsers.equals("1") ) {//admin
            newUser = new K9Admin(cedula: params.cedula, firstName: params.firstName, lastName: params.lastName, loginUser: params.loginUser, loginPass: params.loginPass, birthDate: params.birthDate, cellphone: params.cellphone, eMail: params.eMail, typeContract: params.typeContract, dateContract: params.dateContract, typeUsers: params.typeUsers, enableUser: true, photoUser: params.photoUser, dateInitialAccess: new Date())
        }else if( params.typeUsers.equals("2") ){//instructor
            newUser = new K9Instructor(cedula: params.cedula, firstName: params.firstName, lastName: params.lastName, loginUser: params.loginUser, loginPass: params.loginPass, birthDate: params.birthDate, cellphone: params.cellphone, eMail: params.eMail, typeContract: params.typeContract, dateContract: params.dateContract, typeUsers: params.typeUsers, enableUser: true, photoUser: params.photoUser, approvedCourse: params.approved_course, dateApproved: params.date_approved, dateCredentials: params.date_credentials, specialities: params.specialities)
        }else if( params.typeUsers.equals("3") ){//veterinario
            newUser = new K9Veterinarian(cedula: params.cedula, firstName: params.firstName, lastName: params.lastName, loginUser: params.loginUser, loginPass: params.loginPass, birthDate: params.birthDate, cellphone: params.cellphone, eMail: params.eMail, typeContract: params.typeContract, dateContract: params.dateContract, typeUsers: params.typeUsers, enableUser: true, photoUser: params.photoUser, professionalCard: params.professional_card, dateExpedition: params.date_expedition)
        }else{//manejador
            newUser = new K9Handler(cedula: params.cedula, firstName: params.firstName, lastName: params.lastName, loginUser: params.loginUser, loginPass: params.loginPass, birthDate: params.birthDate, cellphone: params.cellphone, eMail: params.eMail, typeContract: params.typeContract, dateContract: params.dateContract, typeUsers: params.typeUsers, enableUser: true, photoUser: params.photoUser, basicInductionCourse: params.basic_induction_course, dateApprovedInduction: params.date_approved_induction, typeTrainingHandler: params.type_training_handler)
        }
        if(!newUser.validate()){
            render(view:"/k9Admin/register_user", model:[newUser:newUser])
            return
        } else {
            image = ImageIO.read( new File( params.path+"\\"+ params.photoUser ) )
            ImageIO.write(image, params.photoUser.substring(params.photoUser.length()-3,params.photoUser.length()) , new File( "${new File("").getAbsolutePath()}\\web-app\\purpose\\img\\users\\"+params.photoUser ) )
            Sql sql = Sql.newInstance(dataSource)
            if( params.typeUsers.equals("2") ) {//instructor
                sql.execute('insert into k9user(version,photo_user,birth_date,cedula,cellphone,date_contract,e_mail,enable_user,first_name,last_name,login_pass,login_user,type_contract,type_users,approved_course,date_approved,date_credentials,specialities,class) values (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?)',
                        [6,params.photoUser,params.birthDate,params.cedula,params.cellphone,params.dateContract,params.eMail,true,params.firstName,params.lastName,params.loginPass,params.loginUser,params.typeContract,params.typeUsers,params.approved_course,params.date_approved,params.date_credentials,params.specialities,'co.com.seguridog.K9Instructor'])
            }else if( params.typeUsers.equals("3") ){//veterinario
                sql.execute('insert into k9user(version,photo_user,birth_date,cedula,cellphone,date_contract,e_mail,enable_user,first_name,last_name,login_pass,login_user,type_contract,type_users,professional_card,date_expedition,class) values (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?)',
                        [6,params.photoUser,params.birthDate,params.cedula,params.cellphone,params.dateContract,params.eMail,true,params.firstName,params.lastName,params.loginPass,params.loginUser,params.typeContract,params.typeUsers,params.professional_card,params.date_expedition,'co.com.seguridog.K9Veterinarian'])
            }else{//manejador
                sql.execute('insert into k9user(version,photo_user,birth_date,cedula,cellphone,date_contract,e_mail,enable_user,first_name,last_name,login_pass,login_user,type_contract,type_users,approved_course,date_approved,class) values (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?)',
                        [6,params.photoUser,params.birthDate,params.cedula,params.cellphone,params.dateContract,params.eMail,true,params.firstName,params.lastName,params.loginPass,params.loginUser,params.typeContract,params.typeUsers,params.approved_course,params.date_approved,'co.com.seguridog.K9Handler'])
            }
            redirect(controller: "K9Admin", action: "index")
        }
    }

    def register_service (){
        def handler_list = K9Handler.findAllByEnableUser(true)
        def canine_list = Canine.findAllByStateCanine('Activo')
        //def canine_last = Canine.todayAgeCanine.findAllByStateCanine('Activo')
        [handler_list : handler_list, canine_list : canine_list]
    }

    def save_data_service (){
        Sql sql = Sql.newInstance(dataSource)
        sql.execute('insert into work_canine(version,address_rest_canine,address_ubication_work_area,canines_id,date_finish_return_unitk9,date_ubication_work_area,handlers_id,hours_per_day,type_service,user_of_service) values (?,?,?,?,?,?,?,?,?,?)',
                [0,params.addressRestCanine,params.addressWorkArea,params.canine_id,params.dateFinishWork,params.dateWorkArea,params.handler_id,params.hoursPerDay,params.typeService,params.userService])
        redirect(controller: "K9Admin", action: "index")
    }

    def search_user(){}

    def search_canines(){}

    def result_search_user(){
        def result_user
        if (params.search_type_user.equals("1")){
            result_user = K9User.findAllByFirstNameLikeAndEnableUser("%${params.search_text}%", true)
        } else if (params.search_type_user.equals("2")){
            result_user = K9User.findAllByLastNameLikeAndEnableUser("%${params.search_text}%", true)
        } else if (params.search_type_user.equals("3")){
            result_user = K9User.findAllByTypeUsersAndEnableUser(2,true)
        } else if (params.search_type_user.equals("4")){
            result_user = K9User.findAllByTypeUsersAndEnableUser(3,true)
        } else if (params.search_type_user.equals("5")){
            result_user = K9User.findAllByTypeUsersAndEnableUser(4,true)
        } else if (params.search_type_user.equals("6")){
            result_user = K9User.findAllByEnableUser(true)
        }
        [result_user : result_user]
    }

    def result_search_canines(){
        def result_canine
        if (params.search_type_canine.equals("1")){
            result_canine = Canine.findAllByMicroChipIlike("%${params.search_text}%")
        } else if (params.search_type_canine.equals("2")){
            result_canine = Canine.findAllByNameCanineIlike("%${params.search_text}%")
        } else if (params.search_type_canine.equals("3")){
            result_canine = Canine.findAllBySexCanineIlike("%${params.search_text}%")
        } else {
            result_canine = Canine.findAll()
        }
        [result_canine : result_canine]
    }

    def look_profile_canine(){
        def canine_info = Canine.findById(params.id_canine_look)
        def canine_clinic = ClinicHistory.findAllByCanines(canine_info)
        def canine_training = ExerciseAbility.findAllByCanines(canine_info)
        [canine_info : canine_info, canine_clinic : canine_clinic, canine_training : canine_training]
    }

    def edit_profile_canine(){
        def edit_canine = Canine.findById(params.id_canine_edit)
        [edit_canine : edit_canine]
    }

    def save_profile_canine() {
        Sql sql = Sql.newInstance(dataSource)
        sql.executeUpdate('update canine set name_canine = ?, name_father = ?, name_mother = ?, date_birthday = ?, type_race = ?, color_canine = ?, sign_canine = ?, sex_canine = ?, state_canine = ? where id = ?',
                [params.name_canine,params.name_father,params.name_mother,params.birthDate,params.type_race,params.color_canine,params.sign_canine,params.sex_canine,params.state_canine,params.id_canine])
        redirect(controller: "K9Admin", action: "index")
    }

    def look_book_service() {
        def book_service_list = WorkCanine.findById(params.service_id)
        [book_service_list : book_service_list]
    }

    def edit_user(){
        def edit_user
        if (params.edit_user_type.equals("1")) {
            edit_user = K9Admin.findById(params.edit_user_id)
        } else if (params.edit_user_type.equals("2")) {
            edit_user = K9Instructor.findById(params.edit_user_id)
        } else if (params.edit_user_type.equals("3")) {
            edit_user = K9Veterinarian.findById(params.edit_user_id)
        } else if (params.edit_user_type.equals("4")) {
            edit_user = K9Handler.findById(params.edit_user_id)
        }
        [edit_user : edit_user]
    }

    def save_edit_user(){
        Sql sql = Sql.newInstance(dataSource)
        if (params.id_type.equals("1")) {
            sql.executeUpdate('update k9user set cedula = ?, first_name = ?, last_name = ?, login_user = ?, login_pass = ?, birth_date = ?, cellphone = ?, e_mail = ?, type_contract = ?, date_contract = ? where id = ?',
                    [params.cedula, params.firstName, params.lastName, params.loginUser, params.loginPass, params.birthDate, params.cellphone, params.eMail, params.typeContract, params.dateContract, params.id_type_user])
        } else if (params.id_type.equals("2")) {
            sql.executeUpdate('update k9user set cedula = ?, first_name = ?, last_name = ?, login_user = ?, login_pass = ?, birth_date = ?, cellphone = ?, e_mail = ?, type_contract = ?, date_contract = ?, approved_course = ?, date_approved = ?, date_credentials = ?, specialities = ? where id = ?',
                    [params.cedula, params.firstName, params.lastName, params.loginUser, params.loginPass, params.birthDate, params.cellphone, params.eMail, params.typeContract, params.dateContract, params.approved_course, params.date_approved, params.date_credentials, params.ins_specialities, params.id_type_user])
        } else if (params.id_type.equals("3")) {
            sql.executeUpdate('update k9user set cedula = ?, first_name = ?, last_name = ?, login_user = ?, login_pass = ?, birth_date = ?, cellphone = ?, e_mail = ?, type_contract = ?, date_contract = ?, professional_card = ?, date_expedition = ? where id = ?',
                    [params.cedula, params.firstName, params.lastName, params.loginUser, params.loginPass, params.birthDate, params.cellphone, params.eMail, params.typeContract, params.dateContract, params.professional_card, params.date_expedition, params.id_type_user])
        } else if (params.id_type.equals("4")) {
            sql.executeUpdate('update k9user set cedula = ?, first_name = ?, last_name = ?, login_user = ?, login_pass = ?, birth_date = ?, cellphone = ?, e_mail = ?, type_contract = ?, date_contract = ?, basic_induction_course = ?, date_approved_induction = ?, type_training_handler = ? where id = ?',
                    [params.cedula, params.firstName, params.lastName, params.loginUser, params.loginPass, params.birthDate, params.cellphone, params.eMail, params.typeContract, params.dateContract, params.basic_instruction_course, params.date_approved_course, params.type_training, params.id_type_user])
        }
        redirect(controller: "K9Admin", action: "users_list")
    }

    def look_full_profile_user () {
        def full_user
        def full_info
        if (params.full_user_type.equals("1")) {
            full_user = K9Admin.findById(params.full_user_id)
        } else if (params.full_user_type.equals("2")) {
            full_user = K9Instructor.findById(params.full_user_id)
            full_info = ExerciseAbility.findAllByInstructors(full_user)
        } else if (params.full_user_type.equals("3")) {
            full_user = K9Veterinarian.findById(params.full_user_id)
            full_info = ClinicHistory.findAllByMedics(full_user)
        } else if (params.full_user_type.equals("4")) {
            full_user = K9Handler.findById(params.full_user_id)
            full_info = WorkCanine.findAllByHandlers(full_user)
        }
        [full_user : full_user, full_info : full_info]
    }
}
