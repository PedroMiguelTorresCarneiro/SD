package Monitors;

import Monitors.PollStation.*;
import Monitors.IDCheck.*;
import Monitors.EvotingBooth.*;
import Monitors.Logs.*;
import Monitors.ExitPoll.*;

public interface IAll extends IPollStation, IIDCheck, IEvotingBooth, ILogs, IExitPoll {}
