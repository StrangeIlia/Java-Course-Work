package bgty.vt_41.bi.web;

import bgty.vt_41.bi.entity.domain.Playlist;
import bgty.vt_41.bi.entity.domain.User;
import bgty.vt_41.bi.entity.domain.Video;
import bgty.vt_41.bi.entity.dto.ORReject;
import bgty.vt_41.bi.entity.dto.PlaylistsForm;
import bgty.vt_41.bi.entity.enums.EStatus;
import bgty.vt_41.bi.service.PlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/playlists")
public class PlaylistController {
    @Autowired
    PlaylistsService playlistsService;

    @GetMapping(path = {"/", "/index"})
    public Collection<Playlist> getPlaylists(@RequestParam String username) {
        return playlistsService.findByAuthor(username);
    }

    @PostMapping("/create")
    public Object createPlaylists(@RequestBody PlaylistsForm playlistsForm,
                                  Authentication authentication) {
        User pseudoUser = (User) authentication.getPrincipal();
        Playlist createdPlaylist = playlistsService.create(playlistsForm.getName(), pseudoUser.getId());
        if (createdPlaylist == null)
            return new ORReject("Не удалось создать плейлист");
        else
            return new Object() {
                public String status = EStatus.SUCCESS.toString().toLowerCase();
                public Playlist playlist = createdPlaylist;
            };
    }

    @DeleteMapping("/delete")
    public void deletePlaylists(@RequestParam Integer id, Authentication authentication) {
        User pseudoUser = (User) authentication.getPrincipal();
        playlistsService.deleteById(id, pseudoUser.getId());
    }

    @GetMapping("/videos")
    public Collection<Video> getVideos(@RequestParam Integer id) {
        return playlistsService.findVideosById(id);
    }

    @PostMapping("/add_video")
    public void addVideoInPlaylist(@RequestParam Integer videoId,
                                   @RequestParam Integer playlistId,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        playlistsService.addVideoInPlaylist(videoId, playlistId, user.getId());
    }

    @PostMapping("/delete_video")
    public void deleteVideoInPlaylist(@RequestParam Integer videoId,
                                      @RequestParam Integer playlistId,
                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        playlistsService.deleteVideoInPlaylist(videoId, playlistId, user.getId());
    }
}
