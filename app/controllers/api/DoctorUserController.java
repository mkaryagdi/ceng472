package controllers.api;

import controllers.forms.PatientForm;
import jwt.filter.Attrs;
import models.DoctorUser;
import models.PatientUser;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class DoctorUserController extends Controller {

    private FormFactory formFactory;

    public DoctorUserController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result fetch(Long id) {

        DoctorUser verifiedUser = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        if (verifiedUser.getId().equals(id)) {
            return ok(Json.toJson(verifiedUser));
        } else {
            return badRequest("You are not allowed!");
        }
    }

    public Result createPatient() {

        DoctorUser user = request().attrs().get(Attrs.VERIFIED_DOCTOR_USER);

        Form<PatientForm> form = formFactory.form(PatientForm.class).bind(request().body().asJson());

        if (form.hasErrors()) {
            return badRequest("form has errors.");
        }

        PatientForm body = form.get();

        if (PatientUser.finder.query().where().eq("username", body.username).findCount() != 0)
            return badRequest("username already exists.");

        return ok();

    }
//        World world = worldRepository.findOne();
//
//        if (world == null) {
//            return errorHandler.onClientError(NOT_FOUND, "team-register-worldnotfound",
//                    "World does not exist.",
//                    request().method() + " " + request().uri());
//        }
//
//        RLock userLock = redisModule.getRedisson().getLock("user"  + user.getId());
//        userLock.lock(10, TimeUnit.SECONDS);
//
//        Ebean.beginTransaction();
//        try {
//            user.refresh();
//            world.getEntity().refresh();
//            Ebean.commitTransaction();
//        } catch (Exception e) {
//            userLock.unlock();
//            return errorHandler.onServerError("team-register", e, request().method() + " " + request().uri());
//        } finally {
//            Ebean.endTransaction();
//        }
//
//        if (user.getTeam() != null) {
//            userLock.unlock();
//            return errorHandler.onClientError(BAD_REQUEST, "team-register-alreadyregistered",
//                    "user already has a team.",
//                    request().method() + " " + request().uri());
//        }
//
//        if (Team.finder.query().where().eq("name", body.teamName).findCount() != 0) {
//            userLock.unlock();
//            return errorHandler.onClientError(BAD_REQUEST, "team-register-teamname",
//                    "team name already exists.",
//                    request().method() + " " + request().uri());
//        }
//
//        logger.debug("creating team...");
//        try {
//            world.addUser(user);
//        } catch (League.NoSuchBotUserException e) {
//            return errorHandler.onClientError(BAD_REQUEST, "team-register-nosuchbot", e.getMessage(),
//                    request().method() + " " + request().uri());
//        } catch (Exception e) {
//            return errorHandler.onServerError("team-register", e, request().method() + " " + request().uri());
//        } finally {
//            userLock.unlock();
//        }
//
//        Team team = user.getTeam();
//
//        if (team == null) {
//            return errorHandler.onServerError("team-register", new Exception("Team can not be empty."), request().method() + " " + request().uri());
//        }
//
//        RLock teamLock = redisModule.getRedisson().getLock("team" + team.getId());
//        teamLock.lock(10, TimeUnit.SECONDS);
//
//        Formation defaultFormation = Formation.findOne("4-4-2");
//
//        Ebean.beginTransaction();
//        try {
//            team.setName(body.teamName);
//            team.setNationality(body.nationality);
//            team.setFormation(defaultFormation);
//            team.setEnergy(100.0);
//            team.setStadiumLevel(1);
//            team.setBuildingConstruction(BuildingType.STADIUM, null);
//            team.setFacilitiesLevel(1);
//            team.setBuildingConstruction(BuildingType.FACILITIES, null);
//            team.setParkingAreaLevel(1);
//            team.setBuildingConstruction(BuildingType.PARKING_AREA, null);
//            for (Player player: team.getSquad()) {
//                player.setEnergy(100.0);
//                player.setTraining(0);
//            }
//            Ebean.saveAll(team.getSquad());
//            team.save();
//            Ebean.commitTransaction();
//        } catch (Exception e) {
//            return errorHandler.onServerError("team-register", e, request().method() + " " + request().uri());
//        } finally {
//            Ebean.endTransaction();
//            teamLock.unlock();
//        }
//
//        removeAllLeagueCaches(team.getLeague().getId());
//
//        return created(teamMapper.getTeam(team, user));
//    }
}
